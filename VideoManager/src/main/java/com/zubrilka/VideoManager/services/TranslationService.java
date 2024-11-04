package com.zubrilka.VideoManager.services;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.models.Translation;
import com.zubrilka.VideoManager.repositories.TranslationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TranslationService {
    public final TranslationRepository translationRepository;

    public TranslationService(TranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
    }

    /**
     * returns Translation by uuid
     */
    public Translation getTranslationByUuid(UUID uuid) throws NotFoundException {
        return translationRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Translation with uuid: %s not found".formatted(uuid)));
    }
}
