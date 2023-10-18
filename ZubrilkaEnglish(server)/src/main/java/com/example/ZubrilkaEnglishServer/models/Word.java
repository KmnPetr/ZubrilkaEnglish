package com.example.ZubrilkaEnglishServer.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "Word")
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

    public Word() {}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getForeignWord() {return foreignWord;}
    public void setForeignWord(String foreignWord) {this.foreignWord = foreignWord;}
    public String getTranscription() {return transcription;}
    public void setTranscription(String transcription) {this.transcription = transcription;}
    public String getTranslation() {return translation;}
    public void setTranslation(String translation) {this.translation = translation;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getHasVoise() {return hasVoise;}
    public void setHasVoise(String hasVoise) {this.hasVoise = hasVoise;}
    public String getHasImage() {return hasImage;}
    public void setHasImage(String hasImage) {this.hasImage = hasImage;}
    public String getGroupWord() {return groupWord;}
    public void setGroupWord(String groupWord) {this.groupWord = groupWord;}
}
