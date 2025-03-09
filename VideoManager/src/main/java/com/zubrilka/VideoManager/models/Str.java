package com.zubrilka.VideoManager.models;

import lombok.*;

import java.util.UUID;

/**
 * содержит некую строку на одном определенном языке
 * ее озвучку
 * ссылку на похожее слово в словаре в будущем и другая полезная инфа
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Str {
    private String str;
    private String transcription; //устанавливается только в строке основного языка
    private UUID voice_uuid;
}
