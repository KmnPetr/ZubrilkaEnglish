package com.zubrilka.VideoManager.controllers;

import com.zubrilka.VideoManager.models.Video;
import com.zubrilka.VideoManager.models.VideoInfo;
import com.zubrilka.VideoManager.repositories.VideoRepository;
import com.zubrilka.VideoManager.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/video")
public class VideoController {
    private final VideoService videoService;
    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/upload-new")
    public void uploadNewVideo(@RequestParam("file") MultipartFile file) {

        // Проверьте, что файл не пустой
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Файл не может быть пустым");
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        System.out.println("File size: "+file.getSize()+"  filename: "+fileName);

        Video video = null;
        try {
            video = new Video(null,fileName,null, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        videoService.saveVideo(video);
    }
    @GetMapping("/{uuid}")
    public ResponseEntity<byte[]> getVideoById(@PathVariable UUID uuid) {
        Video video = videoService.getVideoByUUID(uuid);

        if (video == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .headers(headers -> {
                    headers.setContentType(MediaType.valueOf("video/mp4"));
                    headers.set("UUID", video.getUuid().toString());
                    headers.set("X-Filename", video.getFileName());
                })
                .body(video.getBytes());
    }

    /**
     * returns a list of information about the videos available in the database
     */
    @GetMapping("/list-video")
    public ResponseEntity<List<VideoInfo>> getListVideo(@AuthenticationPrincipal UserDetails userDetails) throws InterruptedException {

        String usernameTranslator = userDetails.getUsername();
        List<VideoInfo> videoList = videoService.getListVideosByTranslator(usernameTranslator);
        return ResponseEntity.ok(videoList);
    }
}
