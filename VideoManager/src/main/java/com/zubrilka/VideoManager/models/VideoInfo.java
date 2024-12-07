package com.zubrilka.VideoManager.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * класс содержит информацию разного рода о видео
 * а также различные ссылки на сам файл видео и на его перевод и на переводчика
 * класс должен быть безопасен для отправки по сети в качестве dto
 */
@Entity
@Table(name = "video_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VideoInfo {
    @Id
    @Column(name = "uuid", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    @Column(name = "cn_name")
    private String cnName;
    @Column(name = "en_name")
    private String enName;
    @Column(name = "ru_name")
    private String ruName;
    @Column(name = "link_original")
    private String linkOriginal;
    @Column(name = "translator_uuid", insertable = false, updatable = false)
    private UUID translator_uuid;
    @Transient
    private String translator_name; //for the convenience of sending over the network
    @Column(name = "video_uuid",insertable=false, updatable=false)
    private UUID video_uuid;
    @Column(name = "translation_uuid",insertable=false, updatable=false)
    private UUID translation_uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "translator_uuid", referencedColumnName = "uuid")
    @JsonBackReference
    private Person translator;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "translation_uuid", referencedColumnName = "uuid")
    @JsonBackReference
    private Translation translation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_uuid", referencedColumnName = "uuid")
    @JsonBackReference
    private Video video;
}
