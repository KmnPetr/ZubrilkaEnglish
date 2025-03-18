package com.zubrilka.VideoManager.models;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * the object contains a single phrase from the video, a word or a replica of the hero of the video
 * as well as its translation, links to the voiceover and the time of the beginning and end of the phrase playback in the video
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Phrase {
    //What "id" is created in a web application,
    //it is necessary only for the adequate operation of the react.js with the list,
    // most often it is just a point in time at the time of creation
    private Long id;

    private Str cn;
    private Str ru;
    private Str en;
    //TODO при добавлении новых языков их надо добавить в метод findSimilarVoices в VoiceService так как там происходит обход этих полей циклом а рефлексию использовать не хотелось

    private Long startTime; //the start time of the phrase in the video
    private Long endTime; //the end time of the phrase in the video
    private List<Word> words;//the list of words that make up the phrase
}
