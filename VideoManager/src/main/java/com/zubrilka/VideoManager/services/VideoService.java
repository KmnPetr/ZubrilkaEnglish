package com.zubrilka.VideoManager.services;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.models.Person;
import com.zubrilka.VideoManager.models.Video;
import com.zubrilka.VideoManager.models.VideoInfo;
import com.zubrilka.VideoManager.repositories.VideoInfoRepository;
import com.zubrilka.VideoManager.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class VideoService {
    private final VideoRepository videoRepository;
    private final VideoInfoRepository videoInfoRepository;
    private final PersonService personService;
    @Autowired
    public VideoService(VideoRepository videoRepository, VideoInfoRepository videoInfoRepository, PersonService personService) {
        this.videoRepository = videoRepository;
        this.videoInfoRepository = videoInfoRepository;
        this.personService = personService;
    }


    @Transactional
    public void saveVideo(Video video, UUID videoInfoUuid) throws NotFoundException {
        if (video==null||video.getBytes()==null) throw new IllegalArgumentException("Video is null");

        VideoInfo videoInfo = videoInfoRepository.findByUuid(videoInfoUuid).orElseThrow(()->new NotFoundException("VideoInfo with uuid %s not found".formatted(videoInfoUuid)));

        video.setVideoInfoUuid(videoInfo.getUuid());

        UUID videoUuid = videoRepository.findUuidByVideoInfoUuidIfExists(videoInfoUuid);

        if (videoUuid == null){
            videoRepository.save(video);
        } else {
            video.setUuid(videoUuid);
            videoRepository.save(video);
        }
    }

    public Video getVideoByUUID(UUID uuid) {
        return videoRepository.findByUuid(uuid);
    }

    @Transactional
    public void deleteVideo(UUID uuid) {
        if (uuid!=null) videoRepository.deleteById(uuid);
    }

    public Video getVideoByVideoInfoUUID(UUID videoInfoUuid) {
        return videoRepository.findByVideoInfoUuid(videoInfoUuid).orElse(null);
    }
}
