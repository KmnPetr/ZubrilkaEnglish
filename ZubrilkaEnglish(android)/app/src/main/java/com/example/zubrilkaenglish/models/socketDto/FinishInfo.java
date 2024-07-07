package com.example.zubrilkaenglish.models.socketDto;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * класс отправиться на фронт с различной информацией о результатах поединка
 */
public class FinishInfo {


    private ArrayList<Long> mistakes;
    private ArrayList<Long> correctAnswers;

    public ArrayList<Long> getMistakes() {return mistakes;}
    public void setMistakes(ArrayList<Long> mistakes) {this.mistakes = mistakes;}
    public ArrayList<Long> getCorrectAnswers() {return correctAnswers;}
    public void setCorrectAnswers(ArrayList<Long> correctAnswers) {this.correctAnswers = correctAnswers;}

    // Статический метод для создания объекта из JSON
    public static FinishInfo fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, FinishInfo.class);
    }
}
