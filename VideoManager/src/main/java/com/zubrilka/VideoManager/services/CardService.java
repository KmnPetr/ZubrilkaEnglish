package com.zubrilka.VideoManager.services;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.dto.CardTranslDto;
import com.zubrilka.VideoManager.enums.Language;
import com.zubrilka.VideoManager.models.Card;
import com.zubrilka.VideoManager.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class CardService {
    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional
    public UUID createNew(Card card){
        return cardRepository.save(card).getUuid();
    }

    public List<Card> findSimilarCards(String text) {
        return cardRepository.findAll();
    }

    public Card getCardByUuid(UUID uuid) throws NotFoundException {
        return cardRepository.findById(uuid).orElseThrow(()->new NotFoundException("Card this uuid %s not found".formatted(uuid)));
    }

    //добавит новую строку перевода в карточку
    @Transactional
    public Card addTranslationToCard(CardTranslDto dto) throws NotFoundException {
        Card card = cardRepository.findById(dto.getCard_uuid())
                .orElseThrow(() -> new NotFoundException("Card not found"));

        Language language = dto.getLang();

        String newTranslation = dto.getStr();

        card.getTranslation().computeIfAbsent(language, k -> new ArrayList<>()).add(newTranslation);

        System.err.println(card);

        return cardRepository.save(card);
    }
}
