package com.example.zeapp.onlineCompetition.socketDto;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

/**
 * класс дто приносит с сервера состояние юзера
 * находится ли он в поиске соперника или имеет статус занят или офлайн
 * и другую информацию о времени ожидания и количестве пользователей онлайн
 * и другое
 */
@Getter
@Setter
public class StatusInfo {
    public StatusPlayer statusPlayer; //укажет текущий статус пользователя




    // Метод для преобразования объекта DuelInfo в JSON
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}