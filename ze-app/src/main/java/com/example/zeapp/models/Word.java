package com.example.zeapp.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "word")
public class Word {
    @Id
    @Column("id")
    private int id;
    @Column("foreign_word")
    private String foreignWord;
    @Column("transcription")
    private String transcription;
    @Column("translation")
    private String translation;
    @Column("description")
    private String description;
    @Column("has_voise")
    private String hasVoise;
    @Column("has_image")
    private String hasImage;
    @Column("groupwrd")
    private String groupWord;
    @Column("sorting_value")
    private Integer sorting_value;
}
