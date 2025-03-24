package com.zubrilka.VideoManager.dto;

import com.zubrilka.VideoManager.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

/**
 * используется при запросе на добавление новой строки перевода в карточку
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CardTranslDto {

    @NotBlank(message = "Translation string cannot be blank")
    @Size(max = 255, message = "Translation string length must be less than 255 characters")
    private String str;

    @NotNull(message = "Language must be specified")
    private Language lang;

    @NotNull(message = "Card UUID must be specified")
    private UUID card_uuid;
}
