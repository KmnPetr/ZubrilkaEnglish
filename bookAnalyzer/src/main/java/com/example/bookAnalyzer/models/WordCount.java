package com.example.bookAnalyzer.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordCount {
    private String word;
    private int count;
    public WordCount(String word){
        this.word = word;
        this.count = 1;
    }

    public void countPlusPlus() {
        count++;
    }

    public void increaseCount(int count) {
        this.count+=count;
    }
}
