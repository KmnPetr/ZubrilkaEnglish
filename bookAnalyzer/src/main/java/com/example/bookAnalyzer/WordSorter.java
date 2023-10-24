package com.example.bookAnalyzer;

import com.example.bookAnalyzer.bd_service.WordServise;
import com.example.bookAnalyzer.logic.BooksBody;
import com.example.bookAnalyzer.models.Word;
import com.example.bookAnalyzer.models.WordCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


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

    /**
     * назначит словам скаченным из БД процент и количество употреблений в текстах книг
     */
    public void setCount(){
        wordList.forEach(w->{
            if (mapStrings.containsKey(w.getForeignWord())){
                w.setSorting_value(mapStrings.get(w.getForeignWord()).getCount());
            }
        });

    }


    /**
     * выведет в консоль количество слов с нулевым count
     */
    public void printNumberWordsWithZeroCount() {
        List<Word> filteredList = wordList
                .stream()
                .filter(word -> word.getSorting_value()==0)
                .toList();

        System.out.println("Найдено "+ filteredList.size() + " Word с нулевым count.");
    }

    /**
     * выведет в консоль слова из БД имеющие кроме букв еще и другие символы
     */
    public List<Word> collectNonStandardWord(){
        List<Word> nonStandartWords = wordList
                .stream()
                .filter(w->w.getForeignWord().matches(".*[A-Z\\s.,;!?].*"))
                .toList();

        System.out.println("Найдено "+ nonStandartWords.size() + " нестандартных Word.");

        nonStandartWords
                .forEach(w-> System.out.println(
                        w.getForeignWord()+"\t\t\t"
                                + "id: "+w.getId()+ "\t\t\t"
                                +"group: "+ w.getGroupWord()));

        return nonStandartWords;
    }

    /**
     * выведет на экран, все слова, у которых count ниже "x"
     */
    public void printAllWordsWithCountBelowX(int x) {
        System.out.println("/////////////////////////////////////////////////////////////");
        System.out.println("///////////////////Редкоупотребимые слова////////////////////");
        System.out.println("/////////////////////////////////////////////////////////////");
        wordList
                .stream()
                .filter(w->w.getSorting_value()<x||w.getSorting_value()==0)
                .sorted((w1,w2)->w1.compare(w2,w1))
                .forEach(w-> {

                    String str = w.getForeignWord();
                    int spacesToAdd = 30 - str.length();

                    StringBuilder stringBuilder = new StringBuilder(str);
                    if (str.length()<30){
                        for (int i = 0; i < spacesToAdd; i++) {
                            stringBuilder.append(" ");
                        }
                    }else {
                        stringBuilder.append("  ");
                    }
                    String finalString = stringBuilder.toString();

                    System.out.println(finalString + "count: " + w.getSorting_value());
                });
        System.out.println("/////////////////////////////////////////////////////////////");
        System.out.println("/////////////////////////////////////////////////////////////");
    }

    /**
     * выдаст, все слова, у которых count ниже "x"  или равно 0
     */
    public List<Word> getAllWordsWithCountBelowX(int x) {
        List<Word> list = wordList
                .stream()
                .filter(w->w.getSorting_value()<x||w.getSorting_value()==0).toList();
        return list;
    }

    /**
     * сделает запрос в обьект PhrasesSearch на поиск фраз по тексту,
     * чье количество повторений в тексте по обычной мапе оказалось ниже "x"
     */
    public void searchPhrases(int x){
        List<Word> list = getAllWordsWithCountBelowX(x);

        int i = 0;
        for (Word w : list) {
            if (!mapStrings.containsKey(w.getForeignWord())){
                mapStrings.put(w.getForeignWord(),new WordCount(w.getForeignWord()));
            }
            int count = booksBody.getPhrasesSearch().search(w.getForeignWord());

            mapStrings.get(w.getForeignWord()).setCount(count);

            if ((i%10)==0){
                //выполнение метода долгое, поэтому сделаем некий вывод в консоль
                int percent = (int) ((double) i/ list.size()*100);
                System.out.println("Поиск фраз.. "+percent+"%");
            }
            i++;
        }
    }
}
