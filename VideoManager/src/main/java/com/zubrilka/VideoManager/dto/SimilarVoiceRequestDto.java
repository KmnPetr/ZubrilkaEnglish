package com.zubrilka.VideoManager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * запрос на получение похожих voice по тексту
 * не хотелось текст передавать в url параметром потому что могут возникнуть проблемы с различными символами
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SimilarVoiceRequestDto {
    private String text;
    private UUID translation_uuid;
}
