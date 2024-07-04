package com.example.zubrilkaenglish.models;

import com.google.gson.Gson;

import java.util.Map;

public class SocketMessage {
    private SockMessType type;
    private Map<String,String> map;

    public SocketMessage(SockMessType type, Map<String, String> map) {
        this.type = type;
        this.map = map;
    }

    public SockMessType getType() {return type;}
    public void setType(SockMessType type) {this.type = type;}
    public Map<String, String> getMap() {return map;}
    public void setMap(Map<String, String> map) {this.map = map;}

    // Метод для преобразования объекта SocketMessage в JSON
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // Статический метод для создания объекта SocketMessage из JSON
    public static SocketMessage fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, SocketMessage.class);
    }
}
