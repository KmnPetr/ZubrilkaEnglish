package com.example.zeapp.onlineCompetition;

import com.example.zeapp.models.Word;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * это слово предназначенное для соревнований поединков
 * содержит в себе обычный Word
 * различные варианты ответов(переводов) на него включая ложные
 * указание позиции на которой находится правильный ответ
 */
@Getter
@Setter
public class ComplexWord {
    private Word word;
    public static final int numberAnswers = 4; //количество ответов в списке определяет его размер
    private ArrayList<String> listAnswers; //строки возможные переводы слова включая один верный
    private int rightAnswer; //позиция правильного ответа
    private AtomicInteger countReplies = new AtomicInteger(0); //посчитает количество ответивших игроков, чтобы приступить к следующему слову
    private boolean firstRightAnsw = false; //выставляется true когда первый игрок ответил правильно, этому игроку положены начисления очков
}
