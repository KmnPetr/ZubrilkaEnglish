package com.example.zeapp.onlineCompetition.bots;

import com.example.zeapp.models.Person;
import com.example.zeapp.models.SockMessType;
import com.example.zeapp.models.SocketMessage;
import com.example.zeapp.models.UserRole;
import com.example.zeapp.onlineCompetition.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * создаст несколько ботов игроков
 * если игроков онлайн реально мало
 * даже потестить сложно будет без них
 */
@Component
public class botsManager {
    //хранит в себе пользователей находящихся онлайн
    private static final Map<Long, Player> bots = new HashMap<>();
    private static final Integer maxProbability = 70; //максимальная вероятность правильного ответа ботом
    private static final Integer minProbability = 30; //минимальная вероятность правильного ответа ботом

    private final PlayerHolder playerHolder;
    private final CompetitionManager competitionManager;


    public botsManager(PlayerHolder playerHolder, CompetitionManager competitionManager) {
        this.playerHolder = playerHolder;
        this.competitionManager = competitionManager;


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
            case START_INFO,
                 START_COUNTDOWN: logMessage(botId,jsonMessage);
                break;
            case NEXT_WORD: nextWord(botId, jsonMessage);
                break;
            default:
                System.out.println("Unknown type");
                System.out.println(socketMessage.toJson());
                break;
        }
    }

    /**
     * вызывается при получении следующего слова при поединке с сокета
     * отправит случайный ответ с случайной задержкой
     */
    private void nextWord(Long botId, String jsonMessage) {

        Player bot = bots.get(botId);
        Integer rightAnsw = bot.getCurrentDuel().getRightAnswer(); //позиция текущего правильного ответа
        Long curWordId = bot.getCurrentDuel().getCurWordId();


        Integer botAnswer;

        Integer posWord = bot.getCurrentDuel().getCurWordPos(); //текущая позиция слова


        //вероятность правильного ответа
        // вероятность уменьшается в связи с продвежением поединка по списку слов,
        // последнее слово в списке имеет для бота наиболее низкую вероятность правильного ответа
        Integer probability = maxProbability+(minProbability-maxProbability)*(posWord)/(WordListBuilder.sizeDuelList-1);

        System.out.println("Вероятность правильного ответа бота: "+probability);

        // Генерируем число от 0 до 99
        int randomInt = new Random().nextInt(100);

        if (randomInt < probability) {
            // С вероятностью {probability}% выбираем правильный ответ
            botAnswer = rightAnsw;
        } else {
            //с оставшейся вероятностью выберем ложный ответ
            int[] wrongAnswers = new int[ComplexWord.numberAnswers-1];
            boolean wasMissing = false;
            for (int i = 0; i < ComplexWord.numberAnswers; i++) {
                if (i!=rightAnsw){
                    if (!wasMissing){
                        wrongAnswers[i] = i;
                    } else wrongAnswers[(i-1)] = i;
                } else wasMissing = true;
            }
            botAnswer = wrongAnswers[new Random().nextInt(0,wrongAnswers.length)];
        }

        Integer randomDelay = (int) (Math.random() * (3001 - 200)) + 200;// Генерация случайной задержки от 200 до 3000 миллисекунд
        Mono.delay(Duration.ofMillis(randomDelay)).subscribe(tick->
                competitionManager.receiveMessage(
                        botId,
                        new SocketMessage(
                                SockMessType.CLICK_ANSWER,
                                Map.of(
                                        "position", botAnswer.toString(),
                                        "wordId",curWordId.toString()))));
    }

    /**
     * просто залогирует сообщение
     */
    private void logMessage(Long botId, String jsonMessage) {
        System.out.println("botId: "+botId+" Message: "+jsonMessage);
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
                false,
                100,
                null
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
                false,
                100,
                null
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
                false,
                100,
                null
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
                false,
                100,
                null
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
                false,
                100,
                null
        ));
    }
}
