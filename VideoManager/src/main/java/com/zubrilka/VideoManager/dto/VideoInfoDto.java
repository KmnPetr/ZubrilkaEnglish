package com.zubrilka.VideoManager.dto;

import com.zubrilka.VideoManager.models.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VideoInfoDto {
    private UUID uuid;
    private String cnName;
    private String enName;
    private String ruName;
    private String native_lang;
    private String linkOriginal;
    private UUID translator_uuid;
    private String translator_name;
}