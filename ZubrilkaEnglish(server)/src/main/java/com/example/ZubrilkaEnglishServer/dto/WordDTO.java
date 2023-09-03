package com.example.ZubrilkaEnglishServer.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class WordDTO {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(min = 1,max = 400,message = "This field must be between 1 and 400 characters long")
    @NotEmpty(message = "Word should not be empty.")
    private String foreignWord;
    @Size(min = 0,max = 100,message = "The maximum size of this field is 100 characters")
    private String transcription;
    @Size(min = 1,max = 400,message = "This field must be between 1 and 400 characters long")
    @NotEmpty(message = "This field should not be empty.")
    private String translation;
    private String description;
    private String hasVoise;
    private String hasImage;
    private String groupWord;
    private LocalDateTime updatedAt;

    public WordDTO() {}

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
    public LocalDateTime getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}
}
