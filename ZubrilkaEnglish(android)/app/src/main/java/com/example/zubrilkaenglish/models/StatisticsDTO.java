package com.example.zubrilkaenglish.models;


/**
 * для передачи пользователю списком для просмотра таблицы рейтинга
 */
public class StatisticsDTO {
    private String short_name; //имя взятое из обьекта Person
    private Integer place; //место в таблице
    private Long personId;
    private Long points; //заработанные очки
    private String lastEntry; //последний вход пользователя
    private Integer newPoints; //заработанные очки за последние сутки

    public StatisticsDTO() {}

    public StatisticsDTO(String short_name,Integer place, Long personId, Long points, String lastEntry, Integer newPoints) {
        this.short_name = short_name;
        this.place = place;
        this.personId = personId;
        this.points = points;
        this.lastEntry = lastEntry;
        this.newPoints = newPoints;
    }

    public Integer getPlace() {return place;}
    public void setPlace(Integer place) {this.place = place;}
    public String getShort_name() {return short_name;}
    public void setShort_name(String short_name) {this.short_name = short_name;}
    public Long getPersonId() {return personId;}
    public void setPersonId(Long personId) {this.personId = personId;}
    public Long getPoints() {return points;}
    public void setPoints(Long points) {this.points = points;}
    public String getLastEntry() {return lastEntry;}
    public void setLastEntry(String lastEntry) {this.lastEntry = lastEntry;}
    public Integer getNewPoints() {return newPoints;}
    public void setNewPoints(Integer newPoints) {this.newPoints = newPoints;}
}