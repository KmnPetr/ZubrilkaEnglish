package com.zubrilka.VideoManager.controllers;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.dto.CardTranslDto;
import com.zubrilka.VideoManager.dto.SimilarCardRequestDto;
import com.zubrilka.VideoManager.models.Card;
import com.zubrilka.VideoManager.services.CardService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/card")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }


    /**
     * запрос на список card ранее созданных схожих по тексту
     */
    @PostMapping("/list_cards")
    public List<Card> findSimilarCards(@RequestBody SimilarCardRequestDto request) {
        return cardService.findSimilarCards(request.getText());
    }


    @GetMapping("/{uuid}")
    public Card getCardByUuid(@PathVariable UUID uuid) throws NotFoundException {
            return cardService.getCardByUuid(uuid);
    }

    @PostMapping("/create_card")
    public UUID createCard(@Valid @RequestBody Card card){
        return cardService.createNew(card);
    }

    @PostMapping("/add_translation")
    public Card addTranslationToCard(@Valid @RequestBody CardTranslDto dto) throws NotFoundException {
        return cardService.addTranslationToCard(dto);
    }
}
