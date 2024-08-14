package com.zubrilka.zubrilkaenglish.models.socketDto;

import com.google.gson.Gson;

/**
 * обьект дто отправится игроку с сервера в ответ на его клик по одному из вариантов ответов
 */
public class ClickResult {
    private Long idPlayer; //id игрока сделавшаго выбор
    private Integer newHealth; //новое значение здоровья игрока, улучшилось ли оно или ухудшилось вычисляется на фронте
    private Long idWord; //id слова на всякий случай чтоб предотвратить разные временные баги
    private Boolean isRight; //правилен ли был выбор игрока
    private Integer rightPos; //позиция правильного ответа, если отправляется противнику игрока сделавшего выбор, то ставится "null"
    private Integer wrongPos; //позиция неправильно сделанного ответа, чтоб не хранить на фронте, отсылается всем участникам

    public Long getIdPlayer() {return idPlayer;}
    public void setIdPlayer(Long idPlayer) {this.idPlayer = idPlayer;}
    public Integer getNewHealth() {return newHealth;}
    public void setNewHealth(Integer newHealth) {this.newHealth = newHealth;}
    public Long getIdWord() {return idWord;}
    public void setIdWord(Long idWord) {this.idWord = idWord;}
    public Boolean getIsRight() {return isRight;}
    public void setIsRight(Boolean right) {isRight = right;}
    public Integer getRightPos() {return rightPos;}
    public void setRightPos(Integer rightPos) {this.rightPos = rightPos;}
    public Integer getWrongPos() {return wrongPos;}
    public void setWrongPos(Integer wrongPos) {this.wrongPos = wrongPos;}


    // Статический метод для создания объекта SocketMessage из JSON
    public static ClickResult fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ClickResult.class);
    }
}