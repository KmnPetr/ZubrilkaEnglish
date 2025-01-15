package com.zubrilka.VideoManager.services;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.dto.VideoInfoDto;
import com.zubrilka.VideoManager.models.Person;
import com.zubrilka.VideoManager.models.Translation;
import com.zubrilka.VideoManager.models.VideoInfo;
import com.zubrilka.VideoManager.repositories.VideoInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class VideoInfoService {
    private final VideoInfoRepository repository;
    private final PersonService personService;
    private final TranslationService translationService;
    private final VideoService videoService;

    public VideoInfoService(VideoInfoRepository repository, PersonService personService, TranslationService translationService, VideoService videoService) {
        this.repository = repository;
        this.personService = personService;
        this.translationService = translationService;
        this.videoService = videoService;
    }

    /**
     * requests VideoInfo from the database
     */
    public VideoInfo getVideoInfoByUuid(UUID uuid) throws NotFoundException {
        Optional<VideoInfo> videoInfo = repository.findByUuid(uuid);
        if (!videoInfo.isEmpty()) return videoInfo.get();
        else throw new NotFoundException("VideoInfo with uuid - %s not found".formatted(uuid.toString()));
    }

    /**
     * request to update the video information field, for example, update the video name or description
     */
    @Transactional
    public VideoInfo editVideoInfoField(UUID videoInfo_uuid, String fieldName, String newValue) {
        VideoInfo videoInfo = repository.findById(videoInfo_uuid)
                .orElseThrow(() -> new RuntimeException("VideoInfo not found with uuid: " + videoInfo_uuid));

        switch (fieldName) {
            case "cnName":
                videoInfo.setCnName(newValue);
                break;
            case "enName":
                videoInfo.setEnName(newValue);
                break;
            case "ruName":
                videoInfo.setRuName(newValue);
                break;
            case "linkOriginal":
                videoInfo.setLinkOriginal(newValue);
                break;
            default:
                throw new IllegalArgumentException("Updating the " + fieldName + " field is not supported");
        }


        return repository.save(videoInfo);
    }

    @Transactional
    public VideoInfo createNewVideo(String username) {

        Person person = personService.getPersonByName_v2(username);

        VideoInfo newVideoInfo = new VideoInfo(
                null,
                "New video. The cn_name must be defined.",
                "New video. The en_name must be defined.",
                "New video. The ru_name must be defined.",
                null,
                null,
                person.getUuid(),
                username,
                person
        );
        return repository.save(newVideoInfo);
    }

    @Transactional
    public void deleteVideoInfo(String uuid) {
        repository.deleteById(UUID.fromString(uuid));
    }

    public List<VideoInfo> getListVideosByTranslatorName(String usernameTranslator) {
        Person translator = personService.getPersonByName_v2(usernameTranslator);
        List<VideoInfo> list = repository.findVideosByTranslatorUuid(translator.getUuid());
        return list;
    }

}
