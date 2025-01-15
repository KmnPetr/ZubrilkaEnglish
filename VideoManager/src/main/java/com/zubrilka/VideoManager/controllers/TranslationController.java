package com.zubrilka.VideoManager.controllers;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.models.Phrase;
import com.zubrilka.VideoManager.models.Translation;
import com.zubrilka.VideoManager.services.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/translation")
public class TranslationController {
    private final TranslationService translationService;
    @Autowired
    public TranslationController(TranslationService translationService){
        this.translationService = translationService;
    }
    @GetMapping("/{videoInfoUuid}")
    public Translation getTranslation(@PathVariable String videoInfoUuid) throws NotFoundException {
        return translationService.getTranslationByVideoInfoUuid(UUID.fromString(videoInfoUuid));
    }

    /**
     * update the entire list of phrases in the translation
     * even if one line is changed
     * increased server load is possible
     */
    @PutMapping("/{video_info_uuid}/phrases")
    public void updatePhrases(@RequestBody List<Phrase> phrases, @PathVariable String video_info_uuid){
        translationService.updatePhrases(phrases,UUID.fromString(video_info_uuid));
    }
}
