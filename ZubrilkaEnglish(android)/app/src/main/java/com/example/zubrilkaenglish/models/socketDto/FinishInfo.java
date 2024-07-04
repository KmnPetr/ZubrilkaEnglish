package com.example.zubrilkaenglish.models.socketDto;

import com.google.gson.Gson;

/**
 * класс отправиться на фронт с различной информацией о результатах поединка
 */
public class FinishInfo {



    // Статический метод для создания объекта из JSON
    public static FinishInfo fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, FinishInfo.class);
    }
}
