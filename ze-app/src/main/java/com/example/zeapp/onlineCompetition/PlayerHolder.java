package com.example.zeapp.onlineCompetition;

import com.example.zeapp.models.SocketMessage;
import com.example.zeapp.onlineCompetition.socketDto.StatusPlayer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * класс хранит информацию о пользователях находящихся в игре
 * их id, j,обьекты Person, sink для связи с игроком
 */
@Component
public class PlayerHolder {

    //хранит в себе пользователей находящихся онлайн
    private static final Map<Long, Player> playersMap = new HashMap<>();

    //просто геттер
    public Map<Long, Player> getPlayersMap(){return playersMap;}

    /**
     * сохранит новый синк в мапе игроков или создаст нового игрока
     */
    public void saveSink(Long personId, Sinks.Many<String> sink) {
        if (playersMap.containsKey(personId)){
            playersMap.get(personId).setSink(sink);
        } else {
            Player newPlayer = new Player();
            newPlayer.setId(personId);
            newPlayer.setSink(sink);
            newPlayer.setStatusPlayer(StatusPlayer.BUSY);
            playersMap.put(personId,newPlayer);
        }
    }

    /**
     * вызывается при штатном или нештатном закрытии сессии
     */
    public void onCloseSession(Long personId) {
        Player player = playersMap.get(personId);
        player.setSink(null);
        Mono.delay(Duration.ofSeconds(60))
                .map(tick->{
                    if (player.getSink() == null){
                        playersMap.remove(personId);
                        System.out.println("Player with id="+personId+" was deleted.");
                    }
                    return tick;
        }).subscribe();
    }

    /**
     * отошлет пользователю SocketMessage
     * защитит от возможных эксепшенов
      */
    public void sendMessage(long personId, SocketMessage socketMessage) {
        try{
            Player player = playersMap.get(personId);
            if (player!=null&&player.getSink()!=null){
                player.getSink().tryEmitNext(socketMessage.toJson());
            }
        }catch (Exception e){e.printStackTrace();}
    }

    /**
     * положит в Player список id активных карточек пользователя полученных от него
     * защитит от возможных эксепшенов
     */
    public void setListIdActiveCards(Long personId, List<Long> listIdActiveCards) {
        try{
            if (playersMap.containsKey(personId)){
                playersMap.get(personId).setListActiveCards(listIdActiveCards);
            }
        }catch (Exception ignore){}
    }

    /**
     * сохранит ботов в общей мапе с игроками
     */
    public void saveBot(Player bot) {
        playersMap.put(bot.getId(), bot);
    }

    /**
     * выдаст игрока по его id
     */
    public Player getPlayer(long personId) {
        return playersMap.get(personId);
    }
}
