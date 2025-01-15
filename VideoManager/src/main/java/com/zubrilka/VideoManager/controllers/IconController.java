package com.zubrilka.VideoManager.controllers;

import com.zubrilka.VideoManager.models.Icon;
import com.zubrilka.VideoManager.services.IconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * The icon controller class for each video
 */
@RestController
@RequestMapping("/api/icon")
public class IconController {
    private final IconService iconService;
    @Autowired
    public IconController(IconService iconService) {
        this.iconService = iconService;
    }

    @PostMapping("/upload/{videoInfoUuid}")
    public void uploadIcon(@PathVariable("videoInfoUuid") String videoInfoUuid,
                           @RequestParam("file") MultipartFile file){
        if (videoInfoUuid==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameter videoInfo_uuid is null");
        }
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Файл не может быть пустым");
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        System.out.println("File size: "+file.getSize()+"  filename: "+fileName);

        Icon icon = null;
        try {
            icon = new Icon(null, UUID.fromString(videoInfoUuid), file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        iconService.saveOrUpdateIcon(icon,UUID.fromString(videoInfoUuid));
    }


    @GetMapping("/download/{videoInfoUuid}")
    public ResponseEntity<byte[]> getIcon(@PathVariable("videoInfoUuid") UUID videoInfoUuid) {
        Icon icon = iconService.findIconByVideoInfoUuid(videoInfoUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Icon not found"));

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(icon.getBytes());
    }
}
