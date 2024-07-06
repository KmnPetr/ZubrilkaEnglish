package com.example.zeapp.onlineCompetition;

import com.example.zeapp.models.SockMessType;
import com.example.zeapp.models.SocketMessage;
import com.example.zeapp.onlineCompetition.socketDto.ClickResult;
import com.example.zeapp.onlineCompetition.socketDto.StatusInfo;
import com.example.zeapp.onlineCompetition.socketDto.StatusPlayer;
import com.example.zeapp.services.PersonService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * класс занимается обработкой различной логики в онлайн соревновании
 */
@Component
public class CompetitionManager {
    private final PersonService personService;
    private final PlayerHolder playerHolder;
    private final DuelHolder duelHolder;
    private final WordListBuilder wordListBuilder;
    private final Integer numberActiveCards = 30; //укажет количество желаемых активных карточек для поединка

    //заявки поединков на получение следующего слова, сделаем общий поток вызывающий nextWord у поединков чтобы не тратить лишние ресурсы и потоки
    private final ConcurrentLinkedQueue<Duel> appForNextWord = new ConcurrentLinkedQueue<>();

    @Autowired
    public CompetitionManager(PersonService personService, PlayerHolder playerHolder, DuelHolder duelHolder, WordListBuilder wordListBuilder) {
        this.personService = personService;
        this.playerHolder = playerHolder;
        this.duelHolder = duelHolder;
        this.wordListBuilder = wordListBuilder;


        Flux.interval(Duration.ofSeconds(1)).onBackpressureBuffer(1).doOnNext(tick -> duelPicker()).subscribe();
        Flux.interval(Duration.ofMillis(500)).onBackpressureBuffer(1).doOnNext(tick -> readinessChecker()).subscribe();
        Flux.interval(Duration.ofMillis(500)).onBackpressureBuffer(1).doOnNext(tick -> checkAppNextWord()).subscribe();
        Flux.interval(Duration.ofSeconds(1)).onBackpressureBuffer(1).doOnNext(tick -> measureAnswerDelay()).subscribe();
    }

    /**
     * приймет от пользователя сокет сообщение и передаст его в соответствующий метод согласно типу сообщения
     */
    public void receiveMessage(long personId, SocketMessage socketMessage) {
        switch (socketMessage.getType()) {
            case PING:
                receivedPing(personId,socketMessage);
                break;
            case REQUEST_STATUS_INFO:
                sendStatusInfo(personId);
                break;
            case SET_WAITING_STATUS:
                setWaitingStatus(personId);
                break;
            case ACTIVE_CARDS:
                receivedListIdActiveCards(personId,socketMessage);
                break;
            case CLICK_ANSWER:
                userSentAnswer(personId,socketMessage);
                break;
            default:
                System.out.println("Unknown type");
                System.out.println(socketMessage.toJson());
                break;
        }
    }

    /**
     * поставит игрока в режим ожидания поединка и поиска нового противника
     */
    private void setWaitingStatus(long personId) {
        playerHolder.getPlayer(personId).setStatusPlayer(StatusPlayer.WAITING); //установим новый статус
        sendStatusInfo(personId); // отправим пользователю инфу по статусу

    }

    /**
     * отправит игроку данные о его статусе о его сессиях и другое
     */
    private void sendStatusInfo(long personId) {
        Player player = playerHolder.getPlayer(personId);
        StatusInfo statusInfo = new StatusInfo();
        statusInfo.setStatusPlayer(player.getStatusPlayer());

        player.sendMessage(new SocketMessage(SockMessType.STATUS_INFO,Map.of("statusInfo", statusInfo.toJson())));
    }

    /**
     * будет измерять задержки ответа пользователей на слова,
     * чтобы они не думали слишком долго
     * первые 5 секунд бесплатны, следующие 5 сек по -1 здоровью на секунду
     * след 5 сек по -5 на секунду, след все -10 здоровья на секунду
     */
    private void measureAnswerDelay() {
        try {
            Long actualTime = System.currentTimeMillis();

            duelHolder.getDuels().forEach((duelId,duel)->{
                if (duel.players!=null){
                    duel.players.forEach(player -> {
                        if (player!=null&&player.getAnswerStartTime()!=null){
                            int delay = (int) ((actualTime - player.getAnswerStartTime())/1000);

                            if (delay<5){
                                //delay<5 бесплатное ожидание
                            }else if (delay<10){ //-1 от здоровья
                                player.setHealth(player.getHealth()-1);
                                sendPenaltyWaiting(player,duel);
                            } else if (delay<15){ //-5 от здоровья
                                player.setHealth(player.getHealth()-5);
                                sendPenaltyWaiting(player,duel);
                            }else if(delay>=15){ //-10 от здоровья
                                player.setHealth(player.getHealth()-10);
                                sendPenaltyWaiting(player,duel);
                            }
                        }
                    });
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    /**
     * разошлет участникам поединка информацию о превышении времени ожидания ответа
     * что сопровождается штрафами здоровья
     */
    private void sendPenaltyWaiting(Player player, Duel duel) {
        duel.sendToAllPlayers(
                new SocketMessage(
                        SockMessType.PEN_WAIT,
                        Map.of(
                                "idPlayer",player.getId().toString(),
                                "newHealth",player.getHealth().toString()
                        )));
        if (player.getHealth()<=0) finishDuel(duel); //остановим дуель если очки у одного из игроков кончились
    }

    /**
     * сбросит значение answerStartTime у игрока
     * типа он ответил и больше не нужно его штрафовать за задержку времени
     */
    private void resetDelayAnsw(Player player) {
        player.setAnswerStartTime(null);
    }

    /**
     * установит время для дальнейшего измерения задержки пользователем ответа на вопрос
     */
    private void startMeasureDelay(List<Player> players) {
        players.forEach(player -> {
            player.setAnswerStartTime(System.currentTimeMillis());
        });
    }


    /**
     * будет проверять в цикле заявки поединков на получение следующего Word
     * флакс будет вызывать этот метод каждые полсекунды
     * далее он циклом проверяет все элементы списка чьё время наступило делает дело и останавливается
     */
    private synchronized void checkAppNextWord() {
        while (true){
            if (appForNextWord.peek()!=null&&appForNextWord.peek().isTimeNextWord()){
                Duel duel = appForNextWord.poll();
                if (duel!=null){
                    //отправит следующее слово
                    sendNextWord(duel);
                }
            } else break;
        }
    }

    /**
     * закончит поединок
     */
    private void finishDuel(Duel duel) {
        System.out.println("FINISH DUEL");
        duel.getPlayers().forEach(Player::renew);
        duel.sendToAllPlayers(new SocketMessage(SockMessType.FINISH_INFO,Map.of("finishInfo",duel.getFinishInfo().toJson())));
        duelHolder.remove(duel);
    }

    /**
     * обработает выбор пользователем одного из предложенных ответов переводов иностранного слова
     * разсылает результат выбора игрока всем участникам
     * противнику того игрока сделавшего выбор не отсылается правильный вариант ответа
     */
    private void userSentAnswer(long personId, SocketMessage socketMessage) {
        try{
            int posChoice = Integer.parseInt(socketMessage.getMap().get("position"));
            long idWord = Long.parseLong(socketMessage.getMap().get("wordId"));
            Player player = playerHolder.getPlayer(personId);
            Duel duel = duelHolder.getDuelById(player.getCurrentDuelId());

            resetDelayAnsw(player); //сбросит, чтобы по времени не штрафовало

            //проверка что  id слова совпадают чтобы не нарваться на какие задержки в сокете
            //а также защита чтобы юзер не отвечал дважды на один вопрос
            if (idWord == duel.getCurWordId()&&player.getCountAnswer().get()==duel.getCurWordId()){
                int i = player.getCountAnswer().incrementAndGet();
                if (i>(duel.getCurWordId()+1)) {player.getCountAnswer().decrementAndGet();} //даблчек
                else {
                    int rightPos = duel.getRightAnswer();
                    boolean isRight = (rightPos == posChoice);
                    Integer wrongPos = isRight ? null : posChoice;
                    int newHealth = changeHealthByAnswer(isRight,player);


                    ClickResult clickResult = new ClickResult(personId, newHealth, idWord, isRight, rightPos, wrongPos);

                    duel.getPlayers().forEach(it->{
                        if (Objects.equals(it.getId(), player.getId())){
                            clickResult.setRightPos(rightPos);
                        } else {
                            clickResult.setRightPos(null); //противнику не будем отправлять правильный вариант
                        }
                        it.sendMessage(new SocketMessage(SockMessType.CLICK_RESULT,Map.of("clickResult", clickResult.toJson())));
                    });

                    if (!(player.getHealth()<=0)){ //проверим уровень жизни игрока
                        if (duel.incrementAndIsFullCountReplies()){ //инкрементируем поле countReplies и проверит все ли игроки ответили
                            System.out.println("ВСЕ ОТВЕТИЛИ, ПЕРЕЛИСТЫВАЕМ");
                            duel.setNewTimeNextWord(); //установили время для отправки следующего слова
                            appForNextWord.add(duel); //отправим заявку на отправку следующего слова специализированным потоком
                        }
                    }else finishDuel(duel); //остановим дуель если очки у одного из игроков кончились
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }


    /**
     * изменит здоровье игрока в зависимости от того насколько правилен был его ответ
     * за ошибочный ответ снимается 10 очков
     * за правильный начисляется 5
     * здоровье не выходит за рамки от 0 до 100 едениц
     */
    private int changeHealthByAnswer(boolean isRight, Player player) {
        int health = player.getHealth();

        if (isRight){
            health += 5;
        } else health -=10;
        if (health<0) health = 0;
        if (health>100) health = 100;

        player.setHealth(health);
        return health;
    }

    /**
     * метод формирует поединки игроков
     * собирает в них всю необходимую для начала информацию
     * передает в DuelHolder
     */
    private synchronized void duelPicker() {
        System.out.println("количество игроков: "+playerHolder.getPlayersMap().size());
        if(!wordListBuilder.getServiceReady()) return; //сервис формирования списков не готов, уходим.. вернемся позже

        try {
            Map<Long, Player> playersMap = playerHolder.getPlayersMap();
            Set<Long> idSet  = playersMap.keySet();
            List<Long> keyList = new ArrayList<>(idSet);
            Collections.shuffle(keyList); //перемешиваем чтоб одни и те же юзеры не попадались вдвоем

            Duel newDuel = new Duel();
            for (Long key : keyList) {
                Player player = playersMap.get(key);

                if (player.getIsReady() && player.getStatusPlayer()== StatusPlayer.WAITING){
                    newDuel.addPlayers(player);
                }
                if (newDuel.isFull()){
                    newDuel.setDuelsListWords(wordListBuilder.makeListWords(newDuel));; //составляем список для игроков
                    newDuel.setPlayersStatusPlaying(); //устанавливаем их поля как занятые
                    duelHolder.addNewDuel(newDuel); //добавляем в пул поединков, создает также id поединка и ложит это id каждому игроку
                    startDuel(newDuel); //стартуем поединок

                    newDuel = new Duel(); //начинаем формировать новый Duel
                }
            }
        }catch (Exception ignore){}
    }

    /**
     * да начнется
     */
    private void startDuel(Duel newDuel) {
        newDuel.sendStartInfo();
        Flux.just(3, 2, 1).delayElements(Duration.ofSeconds(1))
                .doOnNext(tick->{
                    //отправит тик обратного отсчета перед стартом игры
                    newDuel.sendToAllPlayers(new SocketMessage(SockMessType.START_COUNTDOWN,Map.of("tick",tick.toString())));
                })
                .concatWith(Mono.delay(Duration.ofSeconds(1)).map(Long::intValue))  // добавляет задержку в 1 секунду после последнего элемента
                .doOnComplete(() -> {
                                //отправит первое слово
                                sendNextWord(newDuel);
                        }
                ).subscribe();
    }

    /**
     * отправит следующее слово игрокам
     * проверит, не является ли слово последним
     */
    private void sendNextWord(Duel duel) {
        if (!duel.isWordsEnded()){
            duel.sendToAllPlayers(new SocketMessage(SockMessType.NEXT_WORD,Map.of("nextWord",duel.getNextWord().toJson())));
            duel.getPlayers().forEach(player -> {
                startMeasureDelay(duel.getPlayers()); //установим начальное время для измерения задержки ответа
            });
        } else{
            finishDuel(duel);
        }
    }

    /**
     * обработает полученный от пользователя список id его активных карточек
     * положет его в Player из пула игроков
     */
    private void receivedListIdActiveCards(long personId, SocketMessage socketMessage) {
        String jsonList = socketMessage.getMap().get("listIdActiveCards");
        List<Long> parsedList = null;
        try{
            parsedList = new Gson().fromJson(jsonList,new TypeToken<List<Long>>(){}.getType()); //TODO
        }catch (Exception e){e.printStackTrace();}
        playerHolder.setListIdActiveCards(personId,parsedList);
    }

    /**
     * работает в цикле
     * проверяет готовность пользователей к соревнованию
     * запуск цикла из конструктора этого класса
     */
    private synchronized void readinessChecker(){
        try {//могут выпасть всякие ошибки так как мапа с юзерами плохо потокозащищена
            playerHolder.getPlayersMap().forEach((personId,player) -> {
                boolean isReady = true;
                if (player.getPerson() == null || player.getPerson().getShort_name() == null){ //проверка что игрок подгрузил свои данные из БД и имеет например свое игровое имя
                    isReady = false;
                    //сделаем запрос к БД за обьектом Person
                    personService.findPersonById(personId).doOnNext(player::setPerson).subscribe();
                }
                if (player.getListActiveCards() == null /*|| player.getListActiveCards().size()<numberActiveCards*/){//проверка что игрок предоставил список его карточек находящихся в активном изучении
                    isReady = false;
                        playerHolder.sendMessage(personId, new SocketMessage(
                                                SockMessType.REQUEST_ACTIVE_CARDS,
                                                Collections.singletonMap(
                                                        "numberActiveCards",
                                                        numberActiveCards.toString()
                                                )));
                }

                player.setIsReady(isReady); //установим полученное значение, готов ли игрок
//                System.out.println("Player "+player.getPerson().getShort_name()+" isReady="+player.getIsReady());
            });
        }catch (Exception e){e.printStackTrace();}
    }

    //отошлет юзеру обратно, полученный от него пинг
    private void receivedPing(long personId, SocketMessage socketMessage) {
        playerHolder.sendMessage(personId,socketMessage);
    }
}