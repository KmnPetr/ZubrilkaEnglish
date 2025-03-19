package com.zubrilka.VideoManager.enums;

import java.util.Arrays;
import java.util.List;

public enum LanguageLevel {
    // Китайский (HSK)
    HSK1(Language.cn),
    HSK2(Language.cn),
    HSK3(Language.cn),
    HSK4(Language.cn),
    HSK5(Language.cn),
    HSK6(Language.cn),

    // Английский (CEFR)
    A1(Language.en),
    A2(Language.en),
    B1(Language.en),
    B2(Language.en),
    C1(Language.en),
    C2(Language.en),

    // Русский (ТРКИ)
    TRKI1(Language.ru),
    TRKI2(Language.ru),
    TRKI3(Language.ru),
    TRKI4(Language.ru);

    private final Language language;

    LanguageLevel(Language language) {
        this.language = language;
    }

    public Language getLanguage() {
        return language;
    }

    // Метод для фильтрации уровней по языку
    public static List<LanguageLevel> getLevelsByLanguage(Language language) {
        return Arrays.stream(values())
                .filter(level -> level.getLanguage() == language)
                .toList();
    }
}
