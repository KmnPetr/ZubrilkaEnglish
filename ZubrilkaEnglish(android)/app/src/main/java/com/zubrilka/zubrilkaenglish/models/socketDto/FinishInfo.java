package com.zubrilka.zubrilkaenglish.models.socketDto;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * класс отправиться на фронт с различной информацией о результатах поединка
 */
public class FinishInfo {


    private ArrayList<Long> mistakes;
    private ArrayList<Long> correctAnswers;
    private int[] resultPoints; //очки результата послепоединка
    private ArrayList<String> playersNames; //имена игроков после прохождения поединка
    private int ownPos; //позиция адресата в некоторых списках. напр. "resultPoints" и "playersNames"

    public ArrayList<Long> getMistakes() {return mistakes;}
    public void setMistakes(ArrayList<Long> mistakes) {this.mistakes = mistakes;}
    public ArrayList<Long> getCorrectAnswers() {return correctAnswers;}
    public void setCorrectAnswers(ArrayList<Long> correctAnswers) {this.correctAnswers = correctAnswers;}
    public int[] getResultPoints() {return resultPoints;}
    public void setResultPoints(int[] resultPoints) {this.resultPoints = resultPoints;}
    public ArrayList<String> getPlayersNames() {return playersNames;}
    public void setPlayersNames(ArrayList<String> playersNames) {this.playersNames = playersNames;}
    public int getOwnPos() {return ownPos;}
    public void setOwnPos(int ownPos) {this.ownPos = ownPos;}

    // Статический метод для создания объекта из JSON
    public static FinishInfo fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, FinishInfo.class);
    }
}
