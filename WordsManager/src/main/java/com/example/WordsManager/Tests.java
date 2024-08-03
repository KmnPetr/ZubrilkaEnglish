package com.example.WordsManager;


public class Tests {

    public static void main(String[] args) {
        for (int i = 0; i <= 1000; i++) {
            if (i % 3 == 0 && i % 5 != 0 && sumOfDigits(i) < 10) {
                System.out.println(i);
            }
        }
    }

    // Метод для вычисления суммы цифр числа
    private static int sumOfDigits(int number) {
        int sum = 0;
        String numStr = String.valueOf(number);
        for (char digit : numStr.toCharArray()) {
            sum += Character.getNumericValue(digit);
        }
        return sum;
    }
}