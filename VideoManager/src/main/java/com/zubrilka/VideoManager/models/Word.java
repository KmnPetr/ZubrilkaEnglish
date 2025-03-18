package com.zubrilka.VideoManager.models;

import lombok.*;

/**
 * более подробно о предназначении объекта в {@code Phrase}
 * Каждый обьект Phrase это отдельная фраза,предложение героя видео
 * Она состоит из нескольких Word
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Word {
    private Str cn;
    private Str ru;
    private Str en;
    //TODO при добавлении новых языков их надо добавить в метод findSimilarVoices в VoiceService так как там происходит обход этих полей циклом а рефлексию использовать не хотелось
}