package com.example.WordsManager.services.sortingService;

import com.example.WordsManager.models.Word;
import com.example.WordsManager.models.WordCount;
import com.example.WordsManager.repositories.WordsRepository;
import com.example.WordsManager.services.PropService;
import com.example.WordsManager.services.WordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class WordSorter {
    private List<Word> wordList;
    private Map<String, WordCount> mapStrings;
    private BooksBody booksBody;
    private WordsService wordsService;
    private final PropService propService;
    private final WordsRepository wordsRepository;

    @Autowired
    public WordSorter(BooksBody booksBody, WordsService wordsService, PropService propService, WordsRepository wordsRepository) {
        this.booksBody = booksBody;
        this.wordsService = wordsService;
        this.propService = propService;
        this.wordsRepository = wordsRepository;

        mapStrings = booksBody.getMapStrings();
    }

    /**
     * в общем списке слов отыщет фразовые глаголы по типу "begin/began/begun" они разделены чертами
     * посчитает среднее количество их употреблений в текстах
     * если встречается чтото типа "abide/(abode,abided)/(abode,abided)" посчитает каждое отдельно взятое слово и выведет их среднее значение по частоупотребимости
     */
    public void countIrregularVerbs() {
        if (wordList.isEmpty()) throw new IllegalArgumentException("wordList isEmpty");
        AtomicInteger countVerbs = new AtomicInteger(); //для статистики

        wordList.forEach(word -> {
            if (word.getForeignWord().contains("/")){
                String newStr = word.getForeignWord();
                countVerbs.getAndIncrement();
                //заменяем знаки на пробелы
                newStr = newStr.replaceAll("[/,()]", " ");
                // Разделяем строку на слова по пробелам
                String[] irregularVerbs = newStr.trim().split("\\s+");

                int count = 0;

                for (int i = 0; i < irregularVerbs.length; i++) {
                    if (irregularVerbs[i].isEmpty()) throw new IllegalArgumentException("Один неправильный глагол оказался пустой строкой");

                    if (mapStrings.containsKey(irregularVerbs[i]))
                        count += mapStrings.get(irregularVerbs[i]).getCount();
                    else System.out.println("Не найдено похожих для глагола: "+irregularVerbs[i]+ " id = "+word.getId());
                }
                count = count / irregularVerbs.length;
                word.setSorting_value(count);
            }
        });
        System.out.println("Было пересчитано "+countVerbs.get()+" неправильных глаголов.");
    }

    /**
     * установит список слов к сортировке
     * к вызову обязателен, без него нечего будет сортировать
     */
    public void setWordList(List<Word> wordList) {
        this.wordList = wordList;
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
                                +"group: "+ w.getTopic()));

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

    /**
     * отправит результаты сортировок в БД
     */
    public void updateDateInBD(){
        wordsService.updateSortingValues(wordList);
    }

    /**
     * даем пользовательским телефонам понять, что данные обновились
     */
    public void increaseDictionaryVersion() {
        propService.increaseDictionaryVersion().block();
    }

}