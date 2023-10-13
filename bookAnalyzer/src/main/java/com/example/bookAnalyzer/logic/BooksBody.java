package com.example.bookAnalyzer.logic;

import com.example.bookAnalyzer.models.WordCount;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class BooksBody {

    private List<char[]> charsObject = new ArrayList();
    private Map<String, Integer> mapStrings = new HashMap<>();

    public BooksBody() {

        listPathBooks().forEach(pathBook->{
            char[] chars = makeArrayChars(pathBook);
            charsObject.add(chars);
        });
        fillMapStrings();

    }

    private void fillMapStrings(){
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

        map.values().forEach(it->{
            if (Character.isUpperCase(it.getWord().charAt(0))) {
                String lowerChars = Character.toLowerCase(it.getWord().charAt(0)) + it.getWord().substring(1);
                System.out.println("///////"+lowerChars);
            }
        });

        map.values().stream()
                .sorted((wc1, wc2) -> wc2.getCount() - wc1.getCount())
                .forEach(it-> System.out.println(it.getWord()+"\t"+it.getCount()));
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

        System.out.println("File: "+ filePath);
        System.out.println("chars: "+ chars.length);

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
}
