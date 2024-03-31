package com.example.WordsManager.services;

import com.example.WordsManager.models.Word;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * класс занимается хранением обьектов Word в файле listWords.bin
 */
@Component
@Slf4j
public class SerializedWordStore {

    private final String pathFile = "src/main/resources/serializableWords/listWords.bin";

    //функция оставлена для проверки хранилища сериализованных слов
    public static void main(String[] args) {
        //clearStore() очистиит хранилище сериализованных Words
        //выведем список слов в консоль
        new SerializedWordStore().printAllWords();
    }

    /**
     * очистиит хранилище сериализованных Words
     */
    private synchronized void clearStore() {
        List<Word> emptyList = List.of();
        overwriteListWord(emptyList);
    }

    /**
     * выведет в консоль список всех сериализованных слов
     */
    private synchronized void printAllWords() {
        List<Word> wordList = readListWords();
        for (Word word : wordList) {
            System.out.println(word);
        }
    }

    /**
     * сохранит новое Word в сериализованном хранилище
     */
    public synchronized void addNewWord(Word word) {
        List<Word> wordList = readListWords();
        wordList.add(word);
        overwriteListWord(wordList);
    }

    /**
     * удалит первый в списке сериализованных Words
     * @param firstWord передается для быстрой проверки, что удаляется именно тот word который был взят ранее из хранилища
     */
    public synchronized void deleteFirstWord(Word firstWord) {
        List<Word> listWords = readListWords();
        //быстрая проверка, что слов совпадают тот который первый в листе с тем который превый в списке
        if (firstWord.getForeignWord().equals(listWords.get(0).getForeignWord())
                && firstWord.getTranslation().equals(listWords.get(0).getTranslation())){

            listWords.remove(0);
            overwriteListWord(listWords);

        }else {
            throw new RuntimeException("что-то пошло не так.");
        }
    }

    /**
     * метод вернет превый Word из списка сериализованных Words
     * вернет null, если в хранилище закончились Word
     */
    public synchronized Word getFirtWord() {
        List<Word> wordList = readListWords();
        if (wordList.size()>0){
            return wordList.get(0);
        }else {
            log.info("There are no serialized words.");
            return null;
        }
    }

    /**
     * перезапишет список Word в файле
     * старые данные в файле сотруться и заменятся на новые
     * @param listWords
     */
    private synchronized void overwriteListWord(List<Word> listWords){
        try {
            FileOutputStream fos = new FileOutputStream(pathFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            int i = 0;
            for (Word word : listWords) {
                oos.writeObject(word);
                i++;
            }
            log.info("В файл записано {} обьектов Word",i);

            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * прочитает файл и вернет список обьектов
     * операция безопасна, не приводит к потере данных
     */
    private synchronized List<Word> readListWords() {
        List<Word> list = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(pathFile);
            ObjectInputStream ois = new ObjectInputStream(fis);

            while (true){
                Word word = null;
                try {
                    word = (Word) ois.readObject();
                } catch (EOFException e) {
                    System.out.println("Чтение списка слов закончилось. Размер: "+ list.size());
                     break;
                }

                list.add(word);
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
