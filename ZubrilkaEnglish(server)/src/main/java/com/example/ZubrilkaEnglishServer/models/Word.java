package com.example.ZubrilkaEnglishServer.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Word")
@Getter
@Setter
public class Word {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(min = 1,max = 400,message = "This field must be between 1 and 400 characters long")
    @NotEmpty(message = "Word should not be empty.")
    @Column(name = "foreign_word")
    private String foreignWord;
    @Size(min = 0,max = 100,message = "The maximum size of this field is 100 characters")
    @Column(name = "transcription")
    private String transcription;
    @Size(min = 1,max = 400,message = "This field must be between 1 and 400 characters long")
    @NotEmpty(message = "This field should not be empty.")
    @Column(name = "translation")
    private String translation;
    @Size(min = 0,max = 400,message = "This field must be between 0 and 400 characters long")
    @Column(name = "description")
    private String description;
    @Column(name = "has_voise")
    private String hasVoise;
    @Column(name = "has_image")
    private String hasImage;
    @Column(name = "groupwrd")
    private String groupWord;
    @Column(name = "sorting_value")
    private Integer sorting_value;

    public Word() {}
}
