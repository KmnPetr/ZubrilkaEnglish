package com.example.bookAnalyzer.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@Entity
@Table(name = "Word")
@Getter
@Setter
public class Word implements Comparator<Word> {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(min = 1, max = 400, message = "This field must be between 1 and 400 characters long")
    @NotEmpty(message = "Word should not be empty.")
    @Column(name = "foreign_word")
    private String foreignWord;
    @Size(min = 0, max = 100, message = "The maximum size of this field is 100 characters")
    @Column(name = "transcription")
    private String transcription;
    @Size(min = 1, max = 400, message = "This field must be between 1 and 400 characters long")
    @NotEmpty(message = "This field should not be empty.")
    @Column(name = "translation")
    private String translation;
    @Size(min = 0, max = 400, message = "This field must be between 0 and 400 characters long")
    @Column(name = "description")
    private String description;
//    @Column(name = "has_voise")
//    private String hasVoise;
//    @Column(name = "has_image")
//    private String hasImage;
    @Column(name = "groupwrd")
    private String groupWord;

    //количество раз употреблений в тексте
    @Transient
    private int count;

    public Word() {}

    @Override
    public int compare(Word o1, Word o2) {
        float f = o1.count-o2.count;
        if (f>0)return 1;
        else if (f<0)return -1;
        else return 0;
    }
}
