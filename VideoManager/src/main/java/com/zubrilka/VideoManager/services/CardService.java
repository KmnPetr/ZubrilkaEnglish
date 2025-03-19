package com.zubrilka.VideoManager.services;

import com.zubrilka.VideoManager.enums.Language;
import com.zubrilka.VideoManager.enums.LanguageLevel;
import com.zubrilka.VideoManager.models.Card;
import com.zubrilka.VideoManager.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CardService {
    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;

    }

    @Transactional
    public void createNew(){

        // Создаем объект Card
        Card card = new Card();
        card.setText("Greeting");
        card.setTranscription("[ˈɡriːtɪŋ]");

        // Создаем EnumMap<Language, List<String>> для translation
        EnumMap<Language, List<String>> translations = new EnumMap<>(Language.class);
        translations.put(Language.en, Arrays.asList("Hello", "Hi"));
        translations.put(Language.ru, Arrays.asList("Привет", "Здравствуйте"));
        translations.put(Language.cn, Arrays.asList("你好", "您好"));

        card.setTranslation(translations);
        card.setLanguage(Language.en);
        card.setLevel(LanguageLevel.B1);

        System.err.println("new Card=%s".formatted(card));

        Card createdCard = cardRepository.save(card);

        System.err.println("createdCard=%s".formatted(createdCard));
    }
}
