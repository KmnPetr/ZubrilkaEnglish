package com.zubrilka.VideoManager.models;

import com.zubrilka.VideoManager.enums.Language;
import com.zubrilka.VideoManager.enums.LanguageLevel;
import com.zubrilka.VideoManager.models.converters.TranslationConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

/**
 * содержит некую строку на иностранном для пользователя языке
 * ее перевод, транскрипцию озвучку и другое
 * нужна для начала чтобы слова и фразы из перевода могли ссылаться на нее
 */
@Entity
@Table(name = "card")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Card {
    @Id
    @Column(name = "uuid", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    @Column(name = "text")
    private String text;
    @Column(name = "transcription")
    private String transcription;
    @Column(name = "translation")
    @Convert(converter = TranslationConverter.class)
    private EnumMap<Language, List<String>> translation;
    @Column(name = "language")
    @Enumerated(EnumType.STRING)
    private Language language;
    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    private LanguageLevel level;
    @Column(name = "voice_uuid")
    private UUID voice_uuid;
}
