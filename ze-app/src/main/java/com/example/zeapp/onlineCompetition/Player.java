package com.example.zeapp.onlineCompetition;


import com.example.zeapp.models.Person;
import com.example.zeapp.models.SocketMessage;
import com.example.zeapp.models.UserRole;
import com.example.zeapp.onlineCompetition.socketDto.StatusPlayer;
import lombok.*;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * класс хранит информацию о пользователе находящимся в игре
 * его id, обьект Person, sink для связи с игроком
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Long id;
    private Person person;
    private Sinks.Many<String> sink;
    private List<Long> listActiveCards; //хранит запрошенные у пользователя список его активных карточек из которых потом формируется список карточек для соревнования
    private Boolean isReady; //укажет готов ли игрок и укомплектованы ли его поля
    public static final Integer defaultHealth = 100; //значение здоровья по умолчанию выставляемое TODO, пока не везде в коде пременено ее использование
    private Integer health = 100; //значение здоровья
    private Long currentDuelId = null; //хранит или null или ссылку на текущий поединок
    //хранит время начала отсчета задержки пользователем ответа,
    //при отсутствии необходимости измерения ставится в null
    private Long answerStartTime = null;
    private AtomicInteger countAnswer = new AtomicInteger(0); //защита чтобы юзер не отвечал на один вопрос дважды
    private StatusPlayer statusPlayer = StatusPlayer.BUSY; //статус игрока
    private ArrayList<Long> mistakes = new ArrayList<>(10); //список ошибок, отсылаемые пользователю после поединка
    private ArrayList<Long> correctAnswers = new ArrayList<>(10); //список ошибок, отсылаемые пользователю после поединка
    private Long timeStartWaiting = null; //время начала ожидания перед формированием дуэли
    private boolean loyalToBots = false; //указывает, готов ли юзер играть с ботом

    /**
     * Отошлет пользователю сообщение
     */
    public void sendMessage(SocketMessage socketMessage) {
        if (sink!=null){
            sink.tryEmitNext(socketMessage.toJson());
        }
    }

    /**
     * Обновит пользователя, подготовит его к следующему поединку
     */
    public void renew() {
        if (person.getRole()!= UserRole.BOT)statusPlayer = StatusPlayer.BUSY; //позже игрок сам снимет этот лок, чтобы сформировать следующий поединок
        else statusPlayer = StatusPlayer.WAITING;
        health = 100;
        currentDuelId = null;
        answerStartTime = null;
        countAnswer = new AtomicInteger(0);
        mistakes.clear();
        correctAnswers.clear();
        loyalToBots = false;
    }

    /**
     * Сеттер на здоровье
     * он должен быть в диапазоне от 0 до 100
     */
    public void setHealth(Integer health) {
        if (health<0){
            this.health = 0;
        } else if(health>100){
            this.health = 100;
        }else{
            this.health = health;
        }
    }

    /**
     * Запишет в результаты ошибки и правильные ответы
     */
    public void recordResult(Long wordId, boolean isRight) {
        if (isRight) correctAnswers.add(wordId);
        else mistakes.add(wordId);
    }
}
