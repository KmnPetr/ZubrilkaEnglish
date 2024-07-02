package com.example.zeapp.onlineCompetition.socketDto;

import com.example.zeapp.onlineCompetition.Duel;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * класс предназначен для отправки по сокету
 * содержит информацию о слове предлагаемом обоим игрокам
 * содержит список вариантов ответов на него, позицию правильного ответа и др.
 */
@Getter
@Setter
public class NextWord {
    private Long idWord; //id слова чтобы игрок самостоятельно подтянул на фронте его перевод, озвучку и другое чтобы не перегружать сокет
    private int curWordPos; //укажет какое это слово по счету, счет ведется с "0"
    private int countWords; //укажет общее количество слов, чисто для визуала
    private List<String> listAnswers; //список ответов
    //позицию правильного ответа не указываем, отошлем позже после выбора юзера


    public NextWord(Long idWord, int curWordPos, int countWords, List<String> listAnswers) {
        this.idWord = idWord;
        this.curWordPos = curWordPos;
        this.countWords = countWords;
        this.listAnswers = listAnswers;
    }

    // Метод для преобразования объекта DuelInfo в JSON
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
