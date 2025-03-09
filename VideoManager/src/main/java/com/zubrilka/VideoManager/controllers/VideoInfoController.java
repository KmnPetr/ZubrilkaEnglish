package com.zubrilka.VideoManager.controllers;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.dto.VideoInfoDto;
import com.zubrilka.VideoManager.models.VideoInfo;
import com.zubrilka.VideoManager.services.VideoInfoService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    /**
     * returns a list of information about the videos available in the database
     */
    @GetMapping("/list")
    public List<VideoInfo> getListVideo(@AuthenticationPrincipal UserDetails userDetails) {
        return videoInfoService.getListVideosByTranslatorName(userDetails.getUsername());
    }

    @GetMapping("/{videoUuid}")
    public VideoInfo getVideoInfoByUuid(@PathVariable String videoUuid) throws NotFoundException {
        return videoInfoService.getVideoInfoByUuid(UUID.fromString(videoUuid));
    }

    /**
     * request to update the video information field, for example, update the video name or description
     */
    @PatchMapping
    public VideoInfo editVideoInfoField(
            @PathParam("videoInfo_uuid") String videoInfo_uuid,
            @PathParam("fieldName") String fieldName,
            @PathParam("newValue") String newValue) throws NotFoundException {
        return videoInfoService.editVideoInfoField(UUID.fromString(videoInfo_uuid),fieldName,newValue);
    }

    // Обновляет UsedLanguages требует отдельного эндпоинта,
    // в отличии от остальных полей json список строк плохо адаптируется для передачи по сети как просто строка
    @PatchMapping("/update_list_used_lang")
    public VideoInfo updateUsedLanguagesField(
            @RequestParam UUID videoInfo_uuid,
            @RequestBody List<String> listLang) {

        return videoInfoService.editUsedLanguagesField(videoInfo_uuid, listLang);
    }

    @PostMapping("/create")
    public VideoInfo createNewVideo(@AuthenticationPrincipal UserDetails userDetails){
        return videoInfoService.createNewVideo(userDetails.getUsername());
    }
    @DeleteMapping("/{uuid}")
    public void deleteVideoInfo(@PathVariable String uuid){
        videoInfoService.deleteVideoInfo(uuid);
    }

    private VideoInfoDto convertToDto(VideoInfo vi){
        return new VideoInfoDto(
                vi.getUuid(),
                vi.getCnName(),
                vi.getEnName(),
                vi.getRuName(),
                vi.getNative_lang(),
                vi.getLinkOriginal(),
                vi.getTranslator_uuid(),
                vi.getTranslator_name()
        );
    }
}
