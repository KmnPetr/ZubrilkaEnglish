package com.example.zeapp.onlineCompetition;

import com.example.zeapp.models.SockMessType;
import com.example.zeapp.models.SocketMessage;
import com.example.zeapp.services.PersonService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.flywaydb.core.internal.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.*;

/**
 * класс занимается обработкой различной логики в онлайн соревновании
 */
@Component
public class CompetitionManager {
    private final PersonService personService;
    private final PlayerHolder playerHolder;
    private final DuelHolder duelHolder;
    private final Integer numberActiveCards = 30; //укажет количество желаемых активных карточек для поединка
    private Long numberDuels = 0L;

    @Autowired
    public CompetitionManager(PersonService personService, PlayerHolder playerHolder, DuelHolder duelHolder) {
        this.personService = personService;
        this.playerHolder = playerHolder;
        this.duelHolder = duelHolder;


        Flux.interval(Duration.ofSeconds(1)).onBackpressureBuffer(1).doOnNext(tick -> duelPicker()).subscribe();
        Flux.interval(Duration.ofMillis(500)).onBackpressureBuffer(1).doOnNext(tick -> readinessChecker()).subscribe();
    }

    /**
     * приймет от пользователя сокет сообщение и передаст его в соответствующий метод согласно типу сообщения
     */
    public void receiveMessage(long personId, SocketMessage socketMessage) {
        switch (socketMessage.getType()) {
            case PING:
                receivedPing(personId,socketMessage);
                break;
            case ACTIVE_CARDS:
                receivedListIdActiveCards(personId,socketMessage);
                break;
//            case TYPE3:
//                System.out.println("Handling TYPE3");
//                break;
            default:
                System.out.println("Unknown type");
                System.out.println(socketMessage.toJson());
                break;
        }
    }


    /**
     * метод формирует поединки игроков
     * собирает в них всю необходимую для начала информацию
     * передает в DuelHolder
     */
    public synchronized void duelPicker() {
        try {
            Map<Long, Player> playersMap = playerHolder.getPlayersMap();
            Set<Long> idSet  = playersMap.keySet();
            List<Long> keyList = new ArrayList<>(idSet);
            Collections.shuffle(keyList); //перемешиваем чтоб одни и те же юзеры не попадались вдвоем

            Duel newDuel = new Duel();
            for (Long key : keyList) {
                Player player = playersMap.get(key);

                if (player.getIsReady() && !player.getIsBusy()){
                    newDuel.addPlayers(player);
                }
                if (newDuel.isFull()){
                    makeListWords(newDuel); //составляем список для игроков
                    newDuel.setPlayersBusy(); //устанавливаем их поля как занятые
                    duelHolder.addNewDuel(newDuel); //добавляем в пул поединков
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
        newDuel.sendToAllPlayers(new SocketMessage(SockMessType.START_COMPETITION,Collections.EMPTY_MAP));
        System.out.println("startDuel");  //TODO
    }

    /**
     * составит список слов для поединка
     */
    private void makeListWords(Duel newDuel) {
        //TODO составить список слов для поединка
    }

    /**
     * обработает полученный от пользователя список id его активных карточек
     * положет его в Player из пула игроков
     */
    private void receivedListIdActiveCards(long personId, SocketMessage socketMessage) {
        String jsonList = socketMessage.getMap().get("listIdActiveCards");
        List<Long> parsedList = null;
        try{
            parsedList = new Gson().fromJson(jsonList,new TypeToken<List<Long>>(){}.getType());
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
