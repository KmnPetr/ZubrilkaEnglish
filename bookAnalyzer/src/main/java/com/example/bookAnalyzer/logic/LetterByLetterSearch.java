package com.example.bookAnalyzer.logic;

import java.util.LinkedList;
import java.util.List;

public class LetterByLetterSearch {


    private List<char[]> charsObject;
    String str = "do (my) best";
    char[] lineArr;


    int span = 0;
    int maxSpan = 50;

    int pointerLine = 0;
    Integer lastPointerLine = null;
    boolean lineEnded = false;
    boolean recursiveCalled = false;

    Integer lastIndex = null;
    int i = 0;


    int count = 0;

    public LetterByLetterSearch(List<char[]> charsObject){
        this.charsObject = charsObject;
    }

    public void Run(){
        System.out.println("Строка:\t"+str);
        lineArr = str.toCharArray();

        toLowerChars(lineArr);
        lineArr = removeInParentheses(lineArr);

        System.out.println(lineArr);

        int j=0;
        for (char[] chars : charsObject) {
            j++;
//        String string = "dominated by<<<< fear.3 Hisbestto";
//        char[] chars = string.toCharArray();

            reset();
            lastIndex = null;
            i = 0;


            for (i = 0; i < chars.length; i++) {

                if (lineArr[pointerLine] == chars[i]) {
                    if (pointerLine == 0) {
                        lastIndex = i + 1;
                    }

                    movePointer();

                    if (lineEnded) {
                        //фраза найдена
                        count++;
                        reset();
                        System.out.println("Найдена фраза");
                    }
                } else {
                    //буквы не сошлись
                    if (pointerLine!=0){
                        //сравниваем не превую букву фразы, буквы не сошлись

                        if (recursiveCalled){
                            //рекурсивный вызов был, значит сравниваем первую букву непервого слова, первая буква не совпадает
                            span++;
                            if (span>maxSpan){
                                //спан кончился, сбрасываем
                                reset();
                                i = lastIndex;
                            }
                        } else{
                            //рекурса не было, значит не сошлись средние буквы
                            if(lastPointerLine!=null){
                                //рекурса не было и lastPointerLine не равен null, значит не сошлись СРЕДНИЕ буквы НЕ ПЕРВОГО слова
                                span += (pointerLine - lastPointerLine);
                                if (span>maxSpan){
                                    //спан кончился
                                    reset();
                                    i = lastIndex;
                                }else{
                                    //не сошлись средние буквы, но спан еще не кончился
                                    pointerLine = lastPointerLine;
                                    recursiveCalled = true;
                                    i--;
                                }
                            }else {
                                //рекурса не было и lastPointerLine равен null значит не сошлись средние буквы ПЕРВОГО слова
                                reset();
                                i--;
                            }
                        }
                    }
                }
            }
        }

        System.out.println("Count: "+ count);
    }


    private void movePointer(){

        recursiveCalled = false;

        if (pointerLine<(lineArr.length-1)){
            pointerLine++;
        }else {
            pointerLine = 0;
            lineEnded = true;
        }

        if(!isEnglishChar(lineArr[pointerLine])){
            movePointer();
            recursiveCalled = true;
            lastPointerLine = pointerLine;
        }
    }

    private void reset() {
        pointerLine = 0;
        span = 0;
        recursiveCalled = false;
        lastPointerLine = null;
        lineEnded = false;
    }

    /**
     * изменит все прописные буквы массива на строчные
     */
    private void toLowerChars(char[] chars){
        for (int i = 0; i < chars.length; i++) {
            if (Character.isUpperCase(chars[i])){
                chars[i] = Character.toLowerCase(chars[i]);
            }
        }
    }
    /**
     * удалит из массива чар, то что в скобочках и сами скобочки
     *
     * @return
     */
    private char[] removeInParentheses(char[] chars){
        List<Character> linkedList = new LinkedList<>();
        boolean delete = false;

        for (char c : chars) {
            if (c=='(') delete = true;
            if (!delete){
                linkedList.add(c);
            }
            if (c==')') delete = false;
        }

        char[] newChars = new char[linkedList.size()];

        int i = 0;
        for (Character c : linkedList) {
            newChars[i] = c;
            i++;
        }
        return newChars;
    }


    /**
     * метод вернет true
     * если переданный в параметры char
     * явл. маленькой буквой англ. алфавита
     */
    private boolean isEnglishChar(char c){
        return (c >= 'a' && c <= 'z');
    }
}
