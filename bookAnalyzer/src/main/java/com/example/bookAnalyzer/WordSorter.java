package com.example.bookAnalyzer;

import com.example.bookAnalyzer.bd_service.WordServise;
import com.example.bookAnalyzer.logic.BooksBody;
import com.example.bookAnalyzer.models.Word;
import com.example.bookAnalyzer.models.WordCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class WordSorter {
    private List<Word> wordList;
    private Map<String, WordCount> mapStrings;
    private BooksBody booksBody;
    private WordServise wordServise;

    @Autowired
    public WordSorter(BooksBody booksBody, WordServise wordServise) {
        this.booksBody = booksBody;
        this.wordServise = wordServise;

        wordList = wordServise.getAllWordsFromBd();
        mapStrings = booksBody.getMapStrings();
    }

    public void setPrecent(){
        wordList.forEach(w->{
            if (mapStrings.containsKey(w.getForeignWord())){
                w.setPercent(mapStrings.get(w.getForeignWord()).getPercent());
            }
        });

    }


    /**
     * выведет в консоль все слова с указанием процента употребляемости
     */
    public void printAllWords() {
        wordList
                .stream()
                .sorted((w1,w2)-> w1.compare(w1,w2))
                .forEach(w->
                        System.out.println(w.getForeignWord()+"\t\t\t"+w.getPercent()+" %")
                );
    }

    /**
     * выведет в консоль все слова с нулевым процентом
     */
    public void printWordsWithZeroPercent() {
        wordList
                .stream()
                .filter(word -> word.getPercent()==0)
                .forEach(w->
                        System.out.println(w.getForeignWord()+"\t\t\t"+w.getPercent()+" %")
                );
    }

    /**
     * выведет в консоль количество слов с нулевым процентом
     */
    public void printCountWordsWithZeroPercent() {
        List<Word> filteredList = wordList
                .stream()
                .filter(word -> word.getPercent()==0)
                .toList();

        System.out.println("Найдено "+ filteredList.size() + " Word с нулевым процентом.");
    }

    public List<Word> collectNonStandardWord(){
        List<Word> nonStandartWords = wordList
                .stream()
                .filter(w->w.getForeignWord().matches(".*[A-Z\\s.,;!?].*"))
                .toList();

        System.out.println("Найдено "+ nonStandartWords.size() + " нестандартных Word.");

        nonStandartWords
                .forEach(w-> System.out.println(
                        w.getForeignWord()+"\t\t\t"
                                +w.getPercent()+" %"+ "\t\t\t"
                                + "id: "+w.getId()+ "\t\t\t"
                                +"group: "+ w.getGroupWord()));

        return nonStandartWords;
    }
}
