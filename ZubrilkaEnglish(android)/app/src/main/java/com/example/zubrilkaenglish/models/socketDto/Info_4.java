package com.example.zubrilkaenglish.models.socketDto;


import com.google.gson.Gson;

/**
 * Информация пересылаемая пользователю содержит некоторые данные о времени его ожидания перед стартом поединка,
 * о численности других пользователей и ботов
 */
public class Info_4 {
    private Integer waitingTime; //время ожидания игрока поиска соперника
    private int countPlayers; //количество живых игроков
    private int countBots; //количество именно ожидающих ботов
    private int notInGame; //количество пользователей находящихся не в игре


    public Integer getWaitingTime() {return waitingTime;}
    public void setWaitingTime(Integer waitingTime) {this.waitingTime = waitingTime;}
    public int getCountPlayers() {return countPlayers;}
    public void setCountPlayers(int countPlayers) {this.countPlayers = countPlayers;}
    public int getCountBots() {return countBots;}
    public void setCountBots(int countBots) {this.countBots = countBots;}
    public int getNotInGame() {return notInGame;}
    public void setNotInGame(int notInGame) {this.notInGame = notInGame;}

    // Статический метод для создания объекта из JSON
    public static Info_4 fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Info_4.class);
    }
}
