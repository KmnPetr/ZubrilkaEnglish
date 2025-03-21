package com.zubrilka.VideoManager.dto;

import lombok.*;

/**
 * запрос на получение похожих card по тексту
 * не хотелось текст передавать в url параметром потому что могут возникнуть проблемы с различными символами
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SimilarCardRequestDto {
    private String text;
}
