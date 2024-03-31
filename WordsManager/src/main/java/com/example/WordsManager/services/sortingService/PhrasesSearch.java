package com.example.WordsManager.services.sortingService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * класс занимается сложным поиском фраз, устойчивых слововыражений по тексту
 */
public class PhrasesSearch {

    //это список массивов чаров созданных из книг находящихся в ресурсной папке
    private List<char[]> charsObject;

    //строка фраза в сыром виде. Содержащиеся доп.слова в круглых "()" скобках в дальнейшем удаляться,
    // спецсимволы удаляться,
    //код не чувствителен к количеству пробелов в фразе
    //все прописные буквы понизятся до строчных
    private String str = "";

    //отредактированная строка str
    private char[] lineArr;


    //используется для измерения количества посторонних символов и букв(слов) находящихся в середине фразы
    private int span = 0;
    //максимально допустимое количество посторонних символов и букв(слов) находящихся в середине фразы
    private int maxSpan = 50;

    //указывает на текущий сравниваемый чар в строке lineArr с чаром в тексте книги
    private int pointerLine = 0;

    //выставляедся на чар в строке явл.первой буквой нового слова lineArr,
    // если это первое слово в строке lineArr то lastPointerLine будет null.
    // Подробности в методах movePointer() и reset()
    private Integer lastPointerLine = null;

    //переменная lineEnded станет true, когда будут найдены все чары строки lineArr
    private boolean lineEnded = false;

    //recursiveCalled будет true на первую букву не первого слова. Помогает понять,
    // что началось новое слово, и между ним и предыдущим могут находиться посторонние символы и знаки
    private boolean recursiveCalled = false;

    //если сравнение строки lineArr было не удачно, сравнение продолжиться по массиву книги с индекса lastIndex.
    //по логике, это следующий чар, после того, с которого сравнение пошло не удачно
    private Integer lastIndex = null;

    //просто индекс цикла for(массива книги).
    //Вынес сюда, чтобы с ним можно было работать в других методах класса
    private int i = 0;

    //результат. Количество удачно найденных фраз
    private int count = 0;

    public PhrasesSearch(List<char[]> charsObject){
        this.charsObject = cloneCharsObject(charsObject);
    }

    //тот самый метод, который ищет фразу(устойчивое выражение слов) в тексте книг
    //расчитан на любое количество слов в фразе
    //между словами в массиве книг допускается некоторое количество посторонних символов не содержащихся в искомой фразе,
    //их количество регулируют span и maxSpan.
    //Метод не чувствителен к регистру(все прописные буквы будут понижены)
    //находящаяся доп информация в круглых скобочках не будет учавствовать в поиске
    public int search(String str){


        this.str = str;
        count = 0;

        lineArr = str.toCharArray();

        toLowerChars(lineArr);
        lineArr = removeInParentheses(lineArr);

        for (char[] chars : charsObject) {

            reset();
            lastIndex = null;

            for (i = 0; i < chars.length; i++) {

                if (lineArr[pointerLine] == chars[i]) {
                    if (pointerLine == 0) {
                        lastIndex = i + 1;
                    }

                    movePointer();

                    if (lineEnded) {
                        //фраза найдена
                        if(!isEnglishChar(chars[i+1])&&!isEnglishChar(chars[lastIndex-2])){
                            //проверка, что стоящие перед и после фразы символы не буквы
                            count++;
                            reset();
                        }
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

        return count;
    }

    /**
     * сдвигает указатель pointerLine на шаг вперед, в случае удачного совпадения предыдущего чара
     */
    private void movePointer(){

        recursiveCalled = false;

        if (pointerLine<(lineArr.length-1)){
            pointerLine++;
        }else {
            //строка закончилась, сообщаем об этом при помощи lineEnded
            pointerLine = 0;
            lineEnded = true;
        }

        if(!isEnglishChar(lineArr[pointerLine])){
            //если в искомой строке встретились например пробел или другой символ, вызываем рекурсию метода и сдвигаем еще раз
            movePointer();
            //сообщаем, что началось новое слово
            recursiveCalled = true;
            //помечаем этот чар, чтобы вернуться на этот указатель, если попадется похожая буква, но слово целиком не совпадет
            lastPointerLine = pointerLine;
        }
    }

    /**
     * сбросит значения некоторых вспомогательных переменных, в случае удачного или неудачного нахождения фразы
     */
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

    /**
     * метод сделает все прописные буквы переданного массива в строчные
     * вернет склонированный обьект
     */
    private List<char[]> cloneCharsObject(List<char[]> charsObject){

        List<char[]> cloneCharsObject = new ArrayList<>();

        //клонируем
        for (char[] chars : charsObject) {
            char[] cloneChars = new char[chars.length];

            for (int i = 0; i < chars.length; i++) {
                cloneChars[i] = chars[i];
            }

            cloneCharsObject.add(cloneChars);
        }

        //меняем прописные на строчные
        for (char[] chars : cloneCharsObject) {
            toLowerChars(chars);
        }

        return cloneCharsObject;
    }
}
