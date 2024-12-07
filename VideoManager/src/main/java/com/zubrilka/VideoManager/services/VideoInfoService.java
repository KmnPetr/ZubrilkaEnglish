package com.zubrilka.VideoManager.services;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.models.VideoInfo;
import com.zubrilka.VideoManager.repositories.VideoInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class VideoInfoService {
    private final VideoInfoRepository repository;

    public VideoInfoService(VideoInfoRepository repository) {
        this.repository = repository;
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
}
