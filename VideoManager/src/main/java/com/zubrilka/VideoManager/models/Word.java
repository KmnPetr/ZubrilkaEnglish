package com.zubrilka.VideoManager.models;

import lombok.*;

/**
 * более подробно о предназначении объекта в объекте {@code Phrase}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Word {
    private String cn;
    private String en;
    private String ru;
}