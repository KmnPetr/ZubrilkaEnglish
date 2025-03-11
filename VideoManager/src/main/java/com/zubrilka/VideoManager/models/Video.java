package com.zubrilka.VideoManager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import java.util.UUID;

/**
 * Хранит объект видео
 */
@Entity
@Table(name = "video")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Video {

    @Id
    @Column(name = "uuid", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(name = "video_info_uuid", nullable = false)
    private UUID videoInfoUuid;

    @Column(name = "local_link", nullable = false)
    private String local_link; //ссылка на файл в локальном хранилище сервера
}
