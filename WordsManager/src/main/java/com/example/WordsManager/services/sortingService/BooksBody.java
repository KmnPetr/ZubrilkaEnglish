package com.example.WordsManager.services.sortingService;

import com.example.WordsManager.models.WordCount;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * класс берет книги на английском из репозитория и потрошит их
 */
@Component
@Getter
public class BooksBody {

    private List<char[]> charsObject = new ArrayList();
    private Map<String, WordCount> mapStrings = new HashMap<>();
    private Integer totalNumberWords = 0;
    private PhrasesSearch phrasesSearch;

    public BooksBody() {
        listPathBooks().forEach(pathBook->{
            charsObject.add(makeArrayChars(pathBook));
        });

        mapStrings = fillMapStrings();

        phrasesSearch = new PhrasesSearch(charsObject);
    }

    /**
     * вернет мапу слов на основании заранее подготовленного обьекта чаров(charsObject)
     */
    private Map<String, WordCount> fillMapStrings(){
        Map<String, WordCount> map = new HashMap<>();

        charsObject.forEach(chars -> {

            StringBuilder stringBuilder = new StringBuilder();
            String str = "";

            for (char aChar : chars) {
                if (isEnglishChar(aChar)) {
                    stringBuilder.append(aChar);
                } else {
                    str = stringBuilder.toString();
                    if (!str.isEmpty()) {
                        if (map.containsKey(str)){
                            map.get(str).countPlusPlus();
                        }else {
                            map.put(str,new WordCount(str));
                        }
                    }
                    stringBuilder.delete(0, stringBuilder.length());
                }
            }
        });

        //убираем слова нач. с большой буквы,
        // их значение количества перемещаем в слова нач. с прописной буквы
        List<WordCount> listValues = new LinkedList<>(map.values());

        listValues.forEach(it->{
            if (Character.isUpperCase(it.getWord().charAt(0))) {
                String lowerChars = Character
                        .toLowerCase(it.getWord().charAt(0)) + it.getWord().substring(1);

                if (map.containsKey(lowerChars)){
                    map.get(lowerChars).increaseCount(it.getCount());
                    map.remove(it.getWord());
                }
            }
        });

        //подсчитываем общее количество символов
        int number = 0;
        for (char[] chars : charsObject) {
            number += chars.length;
        }
        System.out.println("Общее количество символов: "+ number);

        //подсчитываем процент употребления слов
        map.values()
                .forEach(it-> totalNumberWords+= it.getCount());

        System.out.println("Общее количество слов: "+ totalNumberWords);

        map.values().forEach(it->it.setPercent(calculatePercent(it)));



        //создаем новые ключи для всех форм глаголов "do", "be" и для "not" с его приставкой "..'t"
        if(map.containsKey("do")
                &&map.containsKey("don")
                &&map.containsKey("does")
                &&map.containsKey("doesn")
                &&map.containsKey("did")
                &&map.containsKey("didn")
                &&map.containsKey("done")
                &&map.containsKey("doing")){
            String DO = "do(don't, does, doesn't, did, didn't, done, doing)";
            map.put(DO,new WordCount(DO));
            map.get(DO).increaseCount(map.get("do").getCount());
            map.get(DO).increaseCount(map.get("don").getCount());
            map.get(DO).increaseCount(map.get("does").getCount());
            map.get(DO).increaseCount(map.get("doesn").getCount());
            map.get(DO).increaseCount(map.get("did").getCount());
            map.get(DO).increaseCount(map.get("didn").getCount());
            map.get(DO).increaseCount(map.get("done").getCount());
            map.get(DO).increaseCount(map.get("doing").getCount());
            map.get(DO).setPercent(calculatePercent(map.get(DO)));
        }
        if(map.containsKey("be")
                &&map.containsKey("m")
                &&map.containsKey("am")
                &&map.containsKey("is")
                &&map.containsKey("isn")
                &&map.containsKey("are")
                &&map.containsKey("aren")
                &&map.containsKey("will")
                &&map.containsKey("ll")
                &&map.containsKey("was")
                &&map.containsKey("wasn")
                &&map.containsKey("were")
                &&map.containsKey("weren")
                &&map.containsKey("been")){
            String BE = "be(m, am, is, isn't, are, aren't, will, ll, was, wasn't, were, weren't, been)";
            map.put(BE,new WordCount(BE));
            map.get(BE).increaseCount(map.get("be").getCount());
            map.get(BE).increaseCount(map.get("am").getCount());
            map.get(BE).increaseCount(map.get("is").getCount());
            map.get(BE).increaseCount(map.get("isn").getCount());
            map.get(BE).increaseCount(map.get("are").getCount());
            map.get(BE).increaseCount(map.get("aren").getCount());
            map.get(BE).increaseCount(map.get("will").getCount());
            map.get(BE).increaseCount(map.get("ll").getCount());
            map.get(BE).increaseCount(map.get("was").getCount());
            map.get(BE).increaseCount(map.get("wasn").getCount());
            map.get(BE).increaseCount(map.get("were").getCount());
            map.get(BE).increaseCount(map.get("weren").getCount());
            map.get(BE).increaseCount(map.get("been").getCount());
            map.get(BE).setPercent(calculatePercent(map.get(BE)));
        }
        if(map.containsKey("not")&&map.containsKey("t")){
            String NOT = "not(...'t)";
            map.put(NOT,new WordCount(NOT));
            map.get(NOT).increaseCount(map.get("not").getCount());
            map.get(NOT).increaseCount(map.get("t").getCount());
            map.get(NOT).setPercent(calculatePercent(map.get(NOT)));
        }

        return map;
    }

    /**
     * выведет в консоль список строчек в порядке их частоупотребимости
     * количество выводимых строчек вводим в параметры, т.к. консоль не всегда справляется с показом более 10000-30000 тыс.строк
     */
    private void printMapStrings(Integer countPrint){
        ArrayList<WordCount> wordCountList = mapStrings.values().stream()
                .sorted((wc1, wc2) -> wc2.getCount() - wc1.getCount())
                .collect(Collectors.toCollection(ArrayList::new));
        int countPrint2;
        if (countPrint>wordCountList.size()) countPrint2=wordCountList.size();
        else countPrint2 = countPrint;

        for (int i = 0; i < countPrint2; i++) {
            System.out.println(
                    (i+1)
                            + ".\t"
                            + wordCountList.get(i).getWord()
                            + "\t"
                            + wordCountList.get(i).getCount()
                            + "\t"
                            + "\t"
                            + "\t"
                            + wordCountList.get(i).getPercent() + "%");
        }
    }


    /**
     * метод подготовит список относительных путей файлов .txt находящихся в папке books
     * выдаст список путей типа: "/books/file.txt"
     */
    public List<String> listPathBooks() {
        String resourcePath = "/books";
        File resourceFolder = null;

        try {
            resourceFolder = new File(getClass().getResource(resourcePath).toURI());
        }catch (Exception e){e.printStackTrace();}

        File[] files = null;
        if (resourceFolder.isDirectory()) {
            files = resourceFolder.listFiles();
        } else {System.out.println("Папка books не найдена.");}

        List<String> listPath = new ArrayList();
        if (files != null) {
            for (File file : files) {
                //добавляем приставку пути
                listPath.add(resourcePath+"/"+file.getName());
            }
        }

        List<String> filteredList = listPath.stream()
                .filter(str -> str.endsWith(".txt"))
                .toList();

        return filteredList;
    }


    /**
     * метод возьмет указанный файл и переведет его в массив char[]
     * метод требует относительный путь до файла .txt
     * путь указывать относительно папки resources. Например: "/books/file.txt"
     */
    private char[] makeArrayChars(String filePath) {
        InputStream in = getClass().getResourceAsStream(filePath);
        char[] chars = null;

        try {
            String fileContent = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            chars = fileContent.toCharArray();
        }catch (Exception e){e.printStackTrace();}

        return chars;
    }

    /**
     * метод вернет true
     * если переданный в параметры char
     * явл. маленькой или заглавной буквой англ. алфавита
     */
    private boolean isEnglishChar(char c){
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * метод вычисляет процент отдельно взятого слова на основании общего количества слов
     * общее количество слов следует вычислять строго до добавления дополнительных ключей в мапу
     */
    private float calculatePercent(WordCount wordCount){
        if (totalNumberWords==null||totalNumberWords==0) throw new RuntimeException("общее количество слов(totalNumberWords) еще не вычеслено");
        return (float) wordCount.getCount() /totalNumberWords*100;
    }
}
