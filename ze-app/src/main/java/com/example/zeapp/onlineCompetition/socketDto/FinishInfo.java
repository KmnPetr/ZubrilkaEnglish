package com.example.zeapp.onlineCompetition.socketDto;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

/**
 * класс отправиться на фронт с различной информацией о результатах поединка
 */
@Getter
@Setter
public class FinishInfo {




    // Метод для преобразования объекта DuelInfo в JSON
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
