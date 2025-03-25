package com.zubrilka.VideoManager.services;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.dto.CardTranslDto;
import com.zubrilka.VideoManager.dto.DeleteCardTranslDto;
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
        return cardRepository.findSimilarVoices(text,100);
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

        return cardRepository.save(card);
    }

    //удалит строку перевода из используемой карточки
    @Transactional
    public Card deleteTranslationFromCard(DeleteCardTranslDto dto) throws NotFoundException {
        Card card = cardRepository.findById(dto.getCard_uuid())
                .orElseThrow(() -> new NotFoundException("Card not found"));

        Language language = dto.getLang();
        String transl = dto.getTransl();

        // Проверяем, содержит ли карта переводы для данного языка
        if (card.getTranslation() != null && card.getTranslation().containsKey(language)) {
            List<String> translations = card.getTranslation().get(language);

            // Удаляем переданный перевод, если он есть в списке
            if (translations.remove(transl)) {
                // Если список стал пустым, удаляем ключ из мапы
                if (translations.isEmpty()) {
                    card.getTranslation().remove(language);
                }

                return cardRepository.save(card);
            }
        }

        return card;
    }
}
