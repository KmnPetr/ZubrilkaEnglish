package com.example.WordsManager;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tests {

    public static void main(String[] args) {



/*
        На вход в приложение получаем строку всегда одинакового вида,
         необходимо обработать эту строку и вывести на экран слова и количество их повторений
        Пример:String str = "one two three one two four five nine"
        output:
        one - 2
        two - 2
        three -1
        five - 1
        nine - 1*/

        String str = "one two three one two four five nine";

        List<String> list = List.of(str.split(" "));

        Map<String,Integer> map = new HashMap<>();

        list.forEach(it ->{
            if(!map.containsKey(it)){
                map.put(it,1);
            }else {
                Integer count = map.get(it);
                map.put(it,count+1);
            }
        });

        map.forEach((k,v)->{
            System.out.println(k+" - "+v);
        });


        n+1







    }

}