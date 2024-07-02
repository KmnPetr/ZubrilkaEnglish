package com.example.zubrilkaenglish.onlineCompetition.socketDto;

import com.google.gson.Gson;

import java.util.List;

/**
 * скопировано с бэка и частично изменено
 * класс содержит информацию о данном текущем поединке
 * предназначен для более гибкого парсинга в json и отправки пользователю на мобилу
 * порядок положения в списках id, имен, и других полей должно строго следовать порядку положения игроков в их списке
 */
public class DuelInfo{
    private Long ownId; //укажет id игрока к которому отправляется сообщение
    private Integer ownPosition; //укажет позицию конкретного игрока в списках, чтобы на фронте легче разбираться
    private List<Long> listId;
    private List<String> listShortNames;
    private List<Integer> listHealth;

    public Long getOwnId() {return ownId;}
    public Integer getOwnPosition() {return ownPosition;}
    public List<Long> getListId() {return listId;}
    public List<String> getListShortNames() {return listShortNames;}
    public List<Integer> getListHealth() {return listHealth;}

    // Статический метод для создания объекта SocketMessage из JSON
    public static DuelInfo fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, DuelInfo.class);
    }
}