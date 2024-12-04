package com.zubrilka.VideoManager.controllers;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.models.VideoInfo;
import com.zubrilka.VideoManager.services.VideoInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * provides various useful information on the video
 * implemented by VideoInfo objects
 * where are the links to the video object itself
 * to the object of translation and other
 */
@RestController
@RequestMapping("/api/video-info")
public class VideoInfoController {
    private final VideoInfoService videoInfoService;
    @Autowired
    public VideoInfoController(VideoInfoService videoInfoService) {
        this.videoInfoService = videoInfoService;
    }

    @GetMapping("/{videoUuid}")
    public VideoInfo getVideoInfoByUuid(@PathVariable String videoUuid) throws NotFoundException {
        return videoInfoService.getVideoInfoByUuid(UUID.fromString(videoUuid));
    }
}
