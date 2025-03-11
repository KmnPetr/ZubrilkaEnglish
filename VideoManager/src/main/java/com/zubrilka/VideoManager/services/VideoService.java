package com.zubrilka.VideoManager.services;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.models.Person;
import com.zubrilka.VideoManager.models.Video;
import com.zubrilka.VideoManager.models.VideoInfo;
import com.zubrilka.VideoManager.repositories.VideoInfoRepository;
import com.zubrilka.VideoManager.repositories.VideoRepository;
import com.zubrilka.VideoManager.util.MediaLocalStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class VideoService {
    private final VideoRepository videoRepository;
    private final VideoInfoRepository videoInfoRepository;
    private final PersonService personService;
    private final MediaLocalStorage mediaLocalStorage;
    @Autowired
    public VideoService(VideoRepository videoRepository, VideoInfoRepository videoInfoRepository, PersonService personService, MediaLocalStorage mediaLocalStorage) {
        this.videoRepository = videoRepository;
        this.videoInfoRepository = videoInfoRepository;
        this.personService = personService;
        this.mediaLocalStorage = mediaLocalStorage;
    }


    @Transactional
    public void saveVideo(MultipartFile file, UUID videoInfoUuid) throws NotFoundException {

        UUID videoUuid = UUID.randomUUID();

        String local_link = mediaLocalStorage.saveVideo(file,videoUuid.toString());

        VideoInfo videoInfo = videoInfoRepository.findByUuid(videoInfoUuid).orElseThrow(()->new NotFoundException("VideoInfo with uuid %s not found".formatted(videoInfoUuid)));

        Video video = new Video(videoUuid,videoInfoUuid,local_link);

        video.setVideoInfoUuid(videoInfo.getUuid());

        UUID videoUuidOld = videoRepository.findUuidByVideoInfoUuidIfExists(videoInfoUuid);

        if (videoUuidOld == null){
            videoRepository.save(video);
        } else {
            videoRepository.deleteById(videoUuidOld);
            mediaLocalStorage.deleteVideo(videoUuidOld.toString());
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

    /**
     * выдаст видео из локального хранилища сервера
     * путь к видео возмет из БД
     */
    public BufferedInputStream getVideoByVideoInfoUUID(UUID videoInfoUuid) throws NotFoundException {
        // Получаем путь к видео с помощью videoService
        Video video = videoRepository.findByVideoInfoUuid(videoInfoUuid).orElseThrow(()->new NotFoundException("Video not found!"));
        String localPath = video.getLocal_link();

        return mediaLocalStorage.getVideoAsStream(localPath);
    }
}
