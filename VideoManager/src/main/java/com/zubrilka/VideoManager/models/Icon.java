package com.zubrilka.VideoManager.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

/**
 * Хранит объект изображение иконку
 */
@Entity
@Table(name = "icon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Icon {
    @Id
    @Column(name = "uuid", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(name = "video_info_uuid", nullable = false)
    private UUID videoInfoUuid;

    @Column(name = "bytes", nullable = false)
    private byte[] bytes;
}