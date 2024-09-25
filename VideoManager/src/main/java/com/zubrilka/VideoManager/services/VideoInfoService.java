package com.zubrilka.VideoManager.services;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.models.VideoInfo;
import com.zubrilka.VideoManager.repositories.VideoInfoRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
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
}
