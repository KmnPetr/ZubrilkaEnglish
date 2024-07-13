package com.example.zeapp.onlineCompetition;

import com.example.zeapp.models.SockMessType;
import com.example.zeapp.models.SocketMessage;
import com.example.zeapp.models.UserRole;
import com.example.zeapp.onlineCompetition.bots.BotsManager;
import com.example.zeapp.onlineCompetition.socketDto.*;
import com.example.zeapp.services.PersonService;
import com.example.zeapp.services.StatisticsServise;
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
    private final StatisticsServise statisticsServise;
    private final Integer numberActiveCards = 30; //укажет количество желаемых активных карточек для поединка
    private int countPlayers = 0; //количество игроков онлайн
    private int notInGame_0 = 0; //количество игроков не в игровой сессии, инкриминируемая вспомогательная переменная
    private int notInGame = 0; //количество игроков не в игровой сессии, считается циклом

    //заявки поединков на получение следующего слова, сделаем общий поток вызывающий nextWord у поединков чтобы не тратить лишние ресурсы и потоки
    private final ConcurrentLinkedQueue<Duel> appForNextWord = new ConcurrentLinkedQueue<>();

    @Autowired
    public CompetitionManager(PersonService personService, PlayerHolder playerHolder, DuelHolder duelHolder, WordListBuilder wordListBuilder, StatisticsServise statisticsServise) {
        this.personService = personService;
        this.playerHolder = playerHolder;
        this.duelHolder = duelHolder;
        this.wordListBuilder = wordListBuilder;
        this.statisticsServise = statisticsServise;


        Flux.interval(Duration.ofSeconds(1)).onBackpressureBuffer(1).doOnNext(tick -> duelPicker()).subscribe();
        Flux.interval(Duration.ofMillis(500)).onBackpressureBuffer(1).doOnNext(tick -> readinessChecker()).subscribe();
        Flux.interval(Duration.ofMillis(500)).onBackpressureBuffer(1).doOnNext(tick -> checkAppNextWord()).subscribe();
        Flux.interval(Duration.ofSeconds(1)).onBackpressureBuffer(1).doOnNext(tick -> measureAnswerDelay()).subscribe();
        Flux.interval(Duration.ofSeconds(1)).onBackpressureBuffer(1).doOnNext(tick -> sendingInfo_4()).subscribe();
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
            case SET_LOYAL_TO_BOTS:
                setLoyalToBots(personId,socketMessage);
                break;
            default:
                System.out.println("Unknown type");
                System.out.println(socketMessage.toJson());
                break;
        }
    }

    /**
     * Обрабатывает намерение пользователя играть с ботами или не играть
     */
    private void setLoyalToBots(long personId, SocketMessage socketMessage) {
        playerHolder.getPlayer(personId).setLoyalToBots(Boolean.parseBoolean(socketMessage.getMap().get("loyalToBots")));
    }

    /**
     * Метод в цикле рассылает участникам различную информацию
     * о времени его ожидания поединка
     * о количестве игроков находящихся онлайн и другое
     */
    private void sendingInfo_4() {
        try {
            countPlayers = playerHolder.getPlayersMap().size() - BotsManager.bots.size();
            //количество незадействованных ботов, пришлось вынести сюда из-за циклической связанности
            int countWaitingBots = (int) BotsManager.bots.values().stream().filter(it->it.getStatusPlayer()==StatusPlayer.WAITING).count();


            Map<String,String> map = new HashMap<>();
            Info_4 info4 = new Info_4();
            info4.setCountPlayers(countPlayers);
            info4.setCountBots(countWaitingBots);
            info4.setNotInGame(notInGame);
            SocketMessage socketMessage = new SocketMessage(SockMessType.INFO_4, map);

            notInGame_0 = 0;//обнулим потом в цикле инкрементируем
            playerHolder.getPlayersMap().forEach((id,player)->{
                if (player.getPerson()!=null && player.getPerson().getRole()!=UserRole.BOT){
                    if (player.getStatusPlayer()==StatusPlayer.BUSY || player.getStatusPlayer()==StatusPlayer.WAITING){
                        notInGame_0++;

                        if (player.getTimeStartWaiting()!=null){
                            info4.setWaitingTime((int)((System.currentTimeMillis()-player.getTimeStartWaiting())/1000));
                        }else info4.setWaitingTime(null);
                        map.put("info_4",info4.toJson());
                        player.sendMessage(socketMessage);
                    }
                }

            });
            notInGame = notInGame_0;
        }catch (Exception e){e.printStackTrace();}
    }

    /**
     * поставит игрока в режим ожидания поединка и поиска нового противника
     */
    private void setWaitingStatus(long personId) {
        playerHolder.getPlayer(personId).setStatusPlayer(StatusPlayer.WAITING); //установим новый статус
        playerHolder.getPlayer(personId).setTimeStartWaiting(System.currentTimeMillis()); //установим время начала ожидания
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
                if (duel.getPlayers()!=null){
                    duel.getPlayers().forEach(player -> {
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
        try{
            while (true){
                if (appForNextWord.peek()!=null&&appForNextWord.peek().isTimeNextWord()){
                    Duel duel = appForNextWord.poll();
                    if (duel!=null){
                        //отправит следующее слово
                        sendNextWord(duel);
                    }
                } else break;
            }
        }catch (Exception e){e.printStackTrace();}
    }

    /**
     * закончит поединок
     */
    private void finishDuel(Duel duel) {
        FinishInfo finishInfo = duel.getFinishInfo();

        finishInfo.setPlayersNames(new ArrayList<>(duel.getPlayers().size()));
        for (Player player: duel.getPlayers()){
            finishInfo.getPlayersNames().add(player.getPerson().getShort_name()); //сложим их имена
        }
        finishInfo.setResultPoints(calculateEarnedPoints(duel)); //посчитаем результаты очков

        for (int i = 0; i < duel.getPlayers().size(); i++) {
            finishInfo.setOwnPos(i);
            finishInfo.setCorrectAnswers(duel.getPlayers().get(i).getCorrectAnswers());
            finishInfo.setMistakes(duel.getPlayers().get(i).getMistakes());
            duel.getPlayers().get(i).sendMessage(new SocketMessage(SockMessType.FINISH_INFO,Map.of("finishInfo",finishInfo.toJson())));//отошлем инфу пользователям
            statisticsServise.updatePoints(duel.getPlayers().get(i).getId(),(long)finishInfo.getResultPoints()[i]).subscribe();//отошлем инфу в БД
        }
        duel.getPlayers().forEach(Player::renew);
        duelHolder.remove(duel);
    }

    /**
     * Посчитает заработанные очки,
     * положит их в FinishInfo для отправки пользователям
     * отправит запрос на сохранение очков в БД
     */
    private int[] calculateEarnedPoints(Duel duel) {
        int[] resultHealth = new int[duel.getPlayers().size()];
        for (int i = 0; i < duel.getPlayers().size(); i++) {
            resultHealth[i] = duel.getPlayers().get(i).getHealth();
        }

        //поиск юзера с наибольшим числом очков
        int largestHealth = resultHealth[0];
        int indexOfLargest = 0;
        for (int i = 1; i < resultHealth.length; i++) {
            if (resultHealth[i] > largestHealth) {
                largestHealth = resultHealth[i];
                indexOfLargest = i;
            }
        }

        //преобразуем число хитпоинтов оставшихся игроков в очки
        for (int i = 0; i < resultHealth.length; i++) {
            if (i!=indexOfLargest&&resultHealth[i]!=largestHealth){ //если очки оказались равны то оставляем
                int countByMiddle = resultHealth[i]-(Player.defaultHealth/2); //посчитаем разницу относительно середины максимального значения здоровья
                int countByLargHP = resultHealth[i]-largestHealth; //посчитаем разницу относительно максимального значения здоровья из группы игроков

                resultHealth[i] = Math.max(countByMiddle, countByLargHP); //выберем наибольшее из двух
            }
        }
        return resultHealth;
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

            //проверка, что id слова совпадают чтобы не нарваться на какие задержки в сокете,
            //а также защита чтобы юзер не отвечал дважды на один вопрос
            if (duel!=null&&idWord == duel.getCurWordId()&&player.getCountAnswer().get()==duel.getCurWordPos()){
                int i = player.getCountAnswer().incrementAndGet();
                if (i>(duel.getCurWordPos()+1)) { //даблчек
                    player.getCountAnswer().decrementAndGet();
                } else {
                    int rightPos = duel.getRightAnswer();
                    boolean isRight = (rightPos == posChoice);
                    Integer wrongPos = isRight ? null : posChoice;
                    int newHealth = changeHealthByAnswer(isRight,player);

                    player.recordResult(duel.getCurWordId(),isRight);

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
        if(!wordListBuilder.getServiceReady()) return; //сервис формирования списков не готов, уходим.. вернемся позже

        try {
            Map<Long, Player> playersMap = playerHolder.getPlayersMap();
            Set<Long> idSet  = playersMap.keySet();
            List<Long> keyList = new ArrayList<>(idSet);
            Collections.shuffle(keyList); //перемешиваем чтоб одни и те же юзеры не попадались вдвоем

            Duel newDuel = new Duel();
            for (Long key : keyList) {
                Player player = playersMap.get(key);

                if (player.getIsReady() && player.getStatusPlayer() == StatusPlayer.WAITING){
                    if (player.getPerson().getRole()!=UserRole.BOT){
                        newDuel.addPlayers(player);
                    } else{ //решим нужен ли нам бот в компашке
                        // не будем брать бота если список только начал формироваться
                        if (newDuel.getPlayers().size()>0){
                            boolean result = true;
                            for (Player player1: newDuel.getPlayers() ){//цикл расчитан на будущее расширение количества игроков в одной сессии:)
                                if (!player1.isLoyalToBots()) result = false;
                            }
                            if (result) newDuel.addPlayers(player); //положили бота в сессию
                        }
                    }
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