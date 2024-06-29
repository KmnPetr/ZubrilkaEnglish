package com.example.zeapp.onlineCompetition;

import com.example.zeapp.models.Word;
import com.example.zeapp.repositories.WordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * класс занимается компоновкой списка слов для поединков игроков
 */
@Component
public class WordListBuilder {
    private final WordsRepository wordsRepository;
    private List<Word> words;
    private Boolean isServiceReady = false;
    private final int sizeDuelList = 20; //количество слов для списка в поединке
    private int countUserWords = 0; //максимальное количество слов взятые у одного игрока требуемые для общего списка, вычисляется позже
    private double shareUsersWords = 0.70; //доля слов взятых у игроков в конечном списке, остальные будут забиты другими случайными словами из глобального списка



    @Autowired
    public WordListBuilder(WordsRepository wordsRepository) {
        this.wordsRepository = wordsRepository;

        prepareDictionaryList();
        calculateCountUserWords();
    }

    /**
     * посчитает максимальное количество слов требуемые от одного юзера
     */
    private void calculateCountUserWords() {
        countUserWords = (int) (sizeDuelList * shareUsersWords)/2;
        System.out.println("countUserWords: "+countUserWords);
    }


    /**
     * подготовит список слов
     * получит из БД
     * отсортирует
     * даст знать что сервис готов к работе
     */
    @Async
    public void prepareDictionaryList() {
            try{
                wordsRepository.findAll().collectList().subscribe(list -> {

                            //отсортирует список слов от большего значения sortValue до меньшего
                            List<Word> collect = list.stream()
                                    .sorted((w1, w2) -> Integer.compare(w2.getSorting_value(), w1.getSorting_value())).toList();

                            collect.forEach(it-> System.out.println(it.getSorting_value()));

                            System.out.println("newList.size() "+collect.size());

                            isServiceReady = true;
                        });
            }catch (Exception e){e.printStackTrace();}
    }

    /**
     * вернет значение готов ли этот класс формировать списки или еще рано
     */
    public Boolean getServiceReady() {
        return isServiceReady;
    }

    /**
     * подготовит список слов
     */
    public void makeListWords(Duel newDuel) {
        List<List<Long>> usersListWords = newDuel.getUsersList();
        List<Long> finalList = new ArrayList<>(sizeDuelList);



    }
}
