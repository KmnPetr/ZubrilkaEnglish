package com.zubrilka.VideoManager.services;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.models.Phrase;
import com.zubrilka.VideoManager.models.Translation;
import com.zubrilka.VideoManager.models.VideoInfo;
import com.zubrilka.VideoManager.repositories.TranslationRepository;
import com.zubrilka.VideoManager.repositories.VideoInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TranslationService {
    public final TranslationRepository translationRepository;
    public final VideoInfoRepository videoInfoRepository;

    public TranslationService(TranslationRepository translationRepository, VideoInfoRepository videoInfoRepository) {
        this.translationRepository = translationRepository;
        this.videoInfoRepository = videoInfoRepository;
    }

    /**
     * returns Translation by uuid
     */
    public Translation getTranslationByUuid(UUID uuid) throws NotFoundException {
        return translationRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Translation with uuid: %s not found".formatted(uuid)));
    }

    @Transactional
    public void updatePhrases(List<Phrase> phrases,UUID videoInfoUuid) {
        Translation translation = translationRepository
            .findByVideoInfoUuid(videoInfoUuid)
            .orElseGet(() -> createNewTranslation(videoInfoUuid));

        translation.setPhrases(phrases);
        translation.setVersion(translation.getVersion()+1);

        translationRepository.save(translation);
    }

    @Transactional
    public Translation createNewTranslation(UUID videoInfoUuid) {
        Translation newTranslation = new Translation(
                null,
                videoInfoUuid,
                0L,
                new ArrayList<>()
        );
        return translationRepository.save(newTranslation);
    }

    @Transactional
    public void deleteTranslation(UUID uuid) {
        if(uuid!=null) translationRepository.deleteById(uuid);
    }

    public Translation getTranslationByVideoInfoUuid(UUID videoInfoUuid) throws NotFoundException {
        return translationRepository.findByVideoInfoUuid(videoInfoUuid).orElse(createNewTranslation(videoInfoUuid));
    }
}

