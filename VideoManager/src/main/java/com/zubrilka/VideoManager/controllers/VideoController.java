package com.zubrilka.VideoManager.controllers;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.services.VideoService;
import com.zubrilka.VideoManager.util.MediaLocalStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/api/video")
public class VideoController {
    private final VideoService videoService;
    @Autowired
    public VideoController(VideoService videoService, MediaLocalStorage mediaLocalStorage) {
        this.videoService = videoService;
    }

    @PostMapping("/upload-new")
    public void uploadNewVideo(
            @RequestParam("videoInfo_uuid") String videoInfoUuid,
            @RequestParam("file") MultipartFile file) throws NotFoundException {

        videoService.saveVideo(file, UUID.fromString(videoInfoUuid));
    }

    @GetMapping("/{videoInfoUuid}")
    public ResponseEntity<StreamingResponseBody> getVideoByVideoInfoUUID(@PathVariable UUID videoInfoUuid) throws NotFoundException {
        BufferedInputStream videoStream = videoService.getVideoByVideoInfoUUID(videoInfoUuid);

        StreamingResponseBody stream = outputStream -> {
            try {
                byte[] buffer = new byte[8192]; // Буфер для чтения данных
                int bytesRead;
                while ((bytesRead = videoStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при передаче видео", e);
            } finally {
                try {
                    videoStream.close(); // Закрытие потока вручную, если необходимо
                } catch (IOException e) {
                    // Логирование ошибки при закрытии потока
                }
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=video.mp4");

        return new ResponseEntity<>(stream, headers, HttpStatus.OK);
    }

}
