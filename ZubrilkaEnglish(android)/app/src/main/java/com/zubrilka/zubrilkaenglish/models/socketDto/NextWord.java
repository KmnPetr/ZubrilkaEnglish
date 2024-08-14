package com.zubrilka.zubrilkaenglish.models.socketDto;

import com.zubrilka.zubrilkaenglish.models.Word;
import com.google.gson.Gson;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * класс предназначен для отправки по сокету
 * содержит информацию о слове предлагаемом обоим игрокам
 * содержит список вариантов ответов на него, позицию правильного ответа и др.
 */
public class NextWord {
    private Long idWord; //id слова чтобы игрок самостоятельно подтянул на фронте его перевод, озвучку и другое чтобы не перегружать сокет
    private int curWordPos; //укажет какое это слово по счету, счет ведется с "0"
    private int countWords; //укажет общее количество слов, чисто для визуала
    private List<String> listAnswers; //список ответов
    @Nullable
    public Word word; //не приходит с бэка, достаем из локальной БД по id
    //позицию правильного ответа не указываем, отошлем позже после выбора юзера

    public Long getIdWord() {return idWord;}
    public void setIdWord(Long idWord) {this.idWord = idWord;}
    public int getCurWordPos() {return curWordPos;}
    public void setCurWordPos(int curWordPos) {this.curWordPos = curWordPos;}
    public int getCountWords() {return countWords;}
    public void setCountWords(int countWords) {this.countWords = countWords;}
    public List<String> getListAnswers() {return listAnswers;}
    public void setListAnswers(List<String> listAnswers) {this.listAnswers = listAnswers;}
    @Nullable
    public Word getWord() {return word;}
    public void setWord(@Nullable Word word) {this.word = word;}

    // Статический метод для создания объекта из JSON
    public static NextWord fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, NextWord.class);
    }
}