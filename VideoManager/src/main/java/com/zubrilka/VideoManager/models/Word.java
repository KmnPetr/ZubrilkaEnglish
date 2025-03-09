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
}