package com.example.zeapp.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "word")
public class Word {
    @Id
    private int id;
    private String foreignWord;
    private String transcription;
    private String translation;
    private String description;
    private String hasVoise;
    private String hasImage;
    private String groupWord;
    private Integer sorting_value;
}
