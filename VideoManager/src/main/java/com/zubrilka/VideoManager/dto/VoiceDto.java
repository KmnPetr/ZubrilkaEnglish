package com.zubrilka.VideoManager.dto;

import com.zubrilka.VideoManager.enums.Sex;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VoiceDto {
    private UUID uuid;
    private String text;
    private String voice;
    private String sex;
    private Integer priority; //используется при поиске похожих голосов актер озвучки употребляемый наибольшее количество раз в переводе в большем приоритете
}
