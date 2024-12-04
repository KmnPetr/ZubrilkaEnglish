package com.zubrilka.VideoManager.controllers;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.models.Translation;
import com.zubrilka.VideoManager.services.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/translation")
public class TranslationController {
    private final TranslationService translationService;
    @Autowired
    public TranslationController(TranslationService translationService){
        this.translationService = translationService;
    }
    @GetMapping("/{translation_uuid}")
    public Translation getTranslation(@PathVariable String translation_uuid) throws NotFoundException {
        UUID uuid = UUID.fromString(translation_uuid);
        return translationService.getTranslationByUuid(uuid);
    }
}
