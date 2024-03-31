package com.example.WordsManager.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.Comparator;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "word")
public class Word implements Serializable, Comparator<Word> {
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
    @Column("link_voice")
    private String link_voice;
    @Column("link_image")
    private String link_image;
    @Column("topic")
    private String topic;
    @Column("sorting_value")
    private Integer sorting_value;


    @Override
    public int compare(Word o1, Word o2) {
        float f = o1.sorting_value-o2.sorting_value;
        if (f>0)return 1;
        else if (f<0)return -1;
        else return 0;
    }
}
