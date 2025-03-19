package com.zubrilka.VideoManager.controllers;

import com.zubrilka.VideoManager.services.CardService;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/card")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }
}
