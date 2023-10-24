package com.example.ZubrilkaEnglishServer.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
    private Integer sorting_value;

    public WordDTO() {}
}