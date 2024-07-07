package com.example.zubrilkaenglish.models.socketDto;

import com.google.gson.Gson;

/**
 * класс дто приносит с сервера состояние юзера
 * находится ли он в поиске соперника или имеет статус занят или офлайн
 * и другую информацию о времени ожидания и количестве пользователей онлайн
 * и другое
 */
public class StatusInfo {
    private StatusPlayer statusPlayer; //укажет текущий статус пользователя


    public StatusPlayer getStatusPlayer() {return statusPlayer;}
    public void setStatusPlayer(StatusPlayer statusPlayer) {this.statusPlayer = statusPlayer;}



    // Статический метод для создания объекта из JSON
    public static StatusInfo fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, StatusInfo.class);
    }
}
