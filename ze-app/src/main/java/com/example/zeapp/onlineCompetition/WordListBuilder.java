package com.example.zeapp.onlineCompetition;

import com.example.zeapp.models.Word;
import com.example.zeapp.repositories.WordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс занимается компоновкой списка слов для поединков игроков
 */
@Component
public class WordListBuilder {
    private final WordsRepository wordsRepository;
    private ArrayList<Word> words;
    private HashMap<Long,Word> mapWords;
    private Boolean isServiceReady = false;
    public static final int sizeDuelList = 25; //количество слов для списка в поединке
    private int countUserWords = 0; //максимальное количество слов взятые у одного игрока требуемые для общего списка, вычисляется позже
    private double shareUsersWords = 0.80; //доля слов взятых у игроков в конечном списке, остальные будут забиты другими случайными словами из глобального списка

    @Autowired
    public WordListBuilder(WordsRepository wordsRepository) {
        this.wordsRepository = wordsRepository;

        prepareDictionaryListAndMap();
        calculateCountUserWords();
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
    public ArrayList<ComplexWord> makeListWords(Duel newDuel) {
        List<List<Long>> usersListWords = newDuel.getUsersList();
        List<Long> finalList = new ArrayList<>(sizeDuelList);

        usersListWords.forEach(list->{
            if (list.size()<=countUserWords){//если список юзера меньше требуемого или пустой - просто перепишем список юзера
                finalList.addAll(list);
            }else {//выберем из списка несколько случайных элементов
                List<Long> copy = new ArrayList<>(list);
                Collections.shuffle(copy);
                finalList.addAll(copy.subList(0, countUserWords));
            }
        });
        finishFinalList(finalList);

        return idListToComplexWordList_AndSort(finalList);
    }

    /**
     * переделает список id слов в готовый список ComplexWord с самим словом и его возможными ответами на него
     */
    private ArrayList<ComplexWord> idListToComplexWordList_AndSort(List<Long> finalList) {
        return finalList.stream()
                .map(id->{
                    ComplexWord complexWord = new ComplexWord();
                    complexWord.setWord(mapWords.get(id));

                    int rightPosition = new Random().nextInt(ComplexWord.numberAnswers);
                    complexWord.setRightAnswer(rightPosition);

                    ArrayList<String> listAnswers = makeListAnswers(id,rightPosition);
                    complexWord.setListAnswers(listAnswers);
                    return complexWord;
                })
                .sorted(((o1, o2) -> Integer.compare(o2.getWord().getSorting_value(),o1.getWord().getSorting_value())))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * составит список ответов включая один правильный
     */
    private ArrayList<String> makeListAnswers(Long idRightAns, int rightPosition) {
        ArrayList<String> listAnswers = new ArrayList<>(ComplexWord.numberAnswers);

        String rightAns = mapWords.get(idRightAns).getTranslation();

        for (int i = 0; i < ComplexWord.numberAnswers; i++) {
            if (i == rightPosition){
                listAnswers.add(rightAns); //положили правильный ответ
            } else {
                listAnswers.add(findWrongAnswer(listAnswers,rightAns));
            }
        }
        return listAnswers;
    }

    /**
     * найдет неправильный ответ
     * проверит чтобы в списке не было похожих
     */
    private String findWrongAnswer(ArrayList<String> listAnswers, String rightAns) {
        int randomPos = new Random().nextInt(words.size());
        String wrongAnswer = mapWords.get((long)words.get(randomPos).getId()).getTranslation(); //найдем рандомный перевод из списка слов
        if (wrongAnswer.equals(rightAns)){ //если неправильный ответ совпал с правильным то вызываем рекурсию
            wrongAnswer = findWrongAnswer(listAnswers,rightAns);
        }
        for (String answ: listAnswers){
            if (wrongAnswer.equals(answ))
                wrongAnswer = findWrongAnswer(listAnswers,rightAns);
        }
        return wrongAnswer;
    }

    /**
     * добьет оставшиеся слова из списка словами из основного словаря
     */
    private void finishFinalList(List<Long> finalList) {
        int remainingQuantity = sizeDuelList - finalList.size();
        if (remainingQuantity>0){
            for (int i = 0; i < remainingQuantity; i++) {
                finalList.add(findRandomWord(finalList));
            }
        }
    }

    /**
     * найдет новое случайное слово, не повторяющееся с предыдущими в списке для поединка
     */
    private Long findRandomWord(List<Long> finalList) {
        int randomPosition = new Random().nextInt(words.size());
        Long idWord = (long) words.get(randomPosition).getId();
        for (Long id: finalList){
            if (Objects.equals(id, idWord)) idWord = findRandomWord(finalList);
        }
        return idWord;
    }

    /**
     * подготовит список слов,
     * получит из БД,
     * отсортирует,
     * даст знать что сервис готов к работе
     */
    @Async
    public void prepareDictionaryListAndMap() {
        try{
            wordsRepository.findAll().collectList().subscribe(list -> {

                //отсортирует список слов от большего значения sortValue до меньшего
                List<Word> collect = list.stream()
                        .sorted((w1, w2) -> Integer.compare(w2.getSorting_value(), w1.getSorting_value())).toList();


                words = new ArrayList<>(collect);
                mapWords = new HashMap<>();
                for (Word word: words){
                    mapWords.put((long) word.getId(),word);
                }

                isServiceReady = true;
            });
        }catch (Exception e){e.printStackTrace();}
    }
    /**
     * посчитает максимальное количество слов требуемые от одного юзера
     */
    private void calculateCountUserWords() {
        countUserWords = (int) (sizeDuelList * shareUsersWords)/2;
    }
}
