package com.example.zeapp.onlineCompetition;


import com.example.zeapp.models.Person;
import com.example.zeapp.models.SocketMessage;
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
    private Boolean isBusy = false; //укажет свободен ли игрок для следующего поединка или на него пока не формировать Duel

    /**
     * отошлет пользователю сообщение
     */
    public void sendMessage(SocketMessage socketMessage) {
        if (sink!=null){
            sink.tryEmitNext(socketMessage.toJson());
        }
    }
}
