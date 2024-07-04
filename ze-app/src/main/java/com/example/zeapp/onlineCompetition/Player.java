package com.example.zeapp.onlineCompetition;


import com.example.zeapp.models.Person;
import com.example.zeapp.models.SocketMessage;
import com.example.zeapp.onlineCompetition.socketDto.StatusPlayer;
import lombok.*;
import reactor.core.publisher.Sinks;

import java.util.List;

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
    private Integer health = 100; //значение здоровья
    private Long currentDuelId = null; //хранит или null или ссылку на текущий поединок
    //хранит время начала отсчета задержки пользователем ответа,
    //при отсутствии необходимости измерения ставится в null
    private Long answerStartTime = null;
    private StatusPlayer statusPlayer = StatusPlayer.BUSY; //статус игрока

    /**
     * отошлет пользователю сообщение
     */
    public void sendMessage(SocketMessage socketMessage) {
        if (sink!=null){
            sink.tryEmitNext(socketMessage.toJson());
        }
    }

    /**
     * обновит пользователя подготовит его к следующему поединку
     */
    public void renew() {
        statusPlayer = StatusPlayer.BUSY; //позже игрок сам снимет этот лок чтобы сформировать следующий поединок
        System.out.println("СТАВИМ ЛОк");
        health = 100;
        currentDuelId = null;
        answerStartTime = null;
    }

    /**
     * сеттер на здоровье
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

}
