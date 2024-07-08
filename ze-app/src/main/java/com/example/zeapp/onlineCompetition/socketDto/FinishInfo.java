package com.example.zeapp.onlineCompetition.socketDto;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Класс отправиться на фронт с различной информацией о результатах поединка
 */
@Getter
@Setter
public class FinishInfo {

    private ArrayList<Long> mistakes;
    private ArrayList<Long> correctAnswers;
    private int[] resultPoints; //очки результата послепоединка
    private ArrayList<String> playersNames; //имена игроков после прохождения поединка
    private int ownPos; //позиция адресата в некоторых списках. напр. "resultPoints" и "playersNames"


    // Метод для преобразования объекта DuelInfo в JSON
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
