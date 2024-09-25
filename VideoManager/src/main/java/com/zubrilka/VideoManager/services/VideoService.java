package com.zubrilka.VideoManager.services;

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
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void saveVideo(Video video) {
        if (video==null||video.getBytes()==null) throw new IllegalArgumentException("Video is null");
        video.setUuid(UUID.randomUUID());
        videoRepository.save(video);
    }

    public Video getVideoByUUID(UUID uuid) {
        return videoRepository.findByUuid(uuid);
    }

    /**
     * вернет выборки список по видео переводом которого занимался конкретный переводчик
     */
    public List<VideoInfo> getListVideosByTranslator(String usernameTranslator) {
        Person person = (Person)personService.loadUserByUsername(usernameTranslator);
        List<VideoInfo> videoInfoList = person.getListVideoInfo();
        videoInfoList.forEach(it -> {
            it.setTranslator_name(person.getUsername());
        });
        return videoInfoList;
    }
}
