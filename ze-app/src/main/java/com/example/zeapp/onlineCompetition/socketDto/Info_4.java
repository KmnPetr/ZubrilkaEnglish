package com.example.zeapp.onlineCompetition.socketDto;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

/**
 * Информация пересылаемая пользователю содержит некоторые данные о времени его ожидания перед стартом поединка,
 * о численности других пользователей и ботов
 */
@Getter
@Setter
public class Info_4 {
    private Integer waitingTime; //время ожидания игрока поиска соперника
    private int countPlayers; //количество живых игроков
    private int countBots; //количество именно ожидающих ботов
    private int notInGame; //количество пользователей находящихся не в игре



    // Метод для преобразования объекта DuelInfo в JSON
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
