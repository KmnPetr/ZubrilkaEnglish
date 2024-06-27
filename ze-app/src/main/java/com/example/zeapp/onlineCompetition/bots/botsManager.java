package com.example.zeapp.onlineCompetition.bots;

import com.example.zeapp.models.Person;
import com.example.zeapp.models.SocketMessage;
import com.example.zeapp.models.UserRole;
import com.example.zeapp.onlineCompetition.Player;
import com.example.zeapp.onlineCompetition.PlayerHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * создаст несколько ботов игроков
 * если игроков онлайн реально мало
 * даже потестить сложно будет без них
 */
@Component
public class botsManager {
    //хранит в себе пользователей находящихся онлайн
    private static Map<Long, Player> bots = new HashMap<>();
    private final PlayerHolder playerHolder;

    public botsManager(PlayerHolder playerHolder) {
        this.playerHolder = playerHolder;


        createBots();
        setListenerSinks();
        sendBotsToPlayerHolder();
    }




    /**
     * реагирует на сообщения приходящие с синков
     */
    private void routeMessage(Long botId, String jsonMessage) {
        SocketMessage socketMessage = SocketMessage.fromJson(jsonMessage);

        switch (socketMessage.getType()) {
            case START_COMPETITION:
                System.out.println(socketMessage.getType()+ " получил бот: "+ bots.get(botId).getPerson().getShort_name());
                break;
            default:
                System.out.println("Unknown type");
                System.out.println(socketMessage.toJson());
                break;
        }
    }

    /**
     * установит слушатели на синки ботов
     */
    private void setListenerSinks() {
        bots.forEach((id,bot)->bot.getSink().asFlux().subscribe(jsonMessage->routeMessage(bot.getId(),jsonMessage)));
    }

    /**
     * отправит ботов в общий с игроками пул
     */
    private void sendBotsToPlayerHolder() {
        bots.forEach((id,bot)-> playerHolder.saveBot(bot) );

    }

    /**
     * создаст несколько ботов
     */
    private void createBots() {
        bots.put(-1L,new Player(
                -1L,
                new Person(
                        -1,
                        "bot1@bot.bot",
                        "tochnoBot",
                        "Tochno_Bot [Bot]",
                        UserRole.BOT,
                        new Timestamp(System.currentTimeMillis())
                ),
                Sinks.many().unicast().onBackpressureBuffer(),
                new ArrayList<>(),
                false,
                false
        ));
        bots.put(-2L,new Player(
                -2L,
                new Person(
                        -2,
                        "bot2@bot.bot",
                        "tochnoBot",
                        "Bip-Bop [Bot]",
                        UserRole.BOT,
                        new Timestamp(System.currentTimeMillis())
                ),
                Sinks.many().unicast().onBackpressureBuffer(),
                new ArrayList<>(),
                false,
                false
        ));
        bots.put(-3L,new Player(
                -3L,
                new Person(
                        -3,
                        "bot3@bot.bot",
                        "tochnoBot",
                        "Hyperdrive [Bot]",
                        UserRole.BOT,
                        new Timestamp(System.currentTimeMillis())
                ),
                Sinks.many().unicast().onBackpressureBuffer(),
                new ArrayList<>(),
                false,
                false
        ));
        bots.put(-4L,new Player(
                -4L,
                new Person(
                        -4,
                        "bot4@bot.bot",
                        "tochnoBot",
                        "TurboByte [Bot]",
                        UserRole.BOT,
                        new Timestamp(System.currentTimeMillis())
                ),
                Sinks.many().unicast().onBackpressureBuffer(),
                new ArrayList<>(),
                false,
                false
        ));
        bots.put(-5L,new Player(
                -5L,
                new Person(
                        -5,
                        "bot5@bot.bot",
                        "tochnoBot",
                        "A bot from America [Bot]",
                        UserRole.BOT,
                        new Timestamp(System.currentTimeMillis())
                ),
                Sinks.many().unicast().onBackpressureBuffer(),
                new ArrayList<>(),
                false,
                false
        ));
    }
}
