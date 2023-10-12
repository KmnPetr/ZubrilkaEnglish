package com.example.bookAnalyzer.logic;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
        ArrayList<Character> string = new ArrayList<Character>();
        string.ensureCapacity(20);

        charsObject.forEach(chars -> {
            for (int i = 0; i < chars.length; i++) {
                if (isEnglishChar(chars[i])){
                    string.add(chars[i]);
                }else{
                    String someWord = String.valueOf(string);
                    string.clear();
                    System.out.println(someWord);
                }
            }
        });
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
