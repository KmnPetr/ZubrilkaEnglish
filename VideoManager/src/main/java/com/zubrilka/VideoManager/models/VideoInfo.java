package com.zubrilka.VideoManager.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

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
@ToString
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
    @Column(name = "native_lang")
    private String native_lang;
    @Column(name = "link_original")
    private String linkOriginal;
    @Column(name = "translator_uuid", insertable = false, updatable = false)
    private UUID translator_uuid;
    @Transient
    private String translator_name; //for the convenience of sending over the network

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "translator_uuid", referencedColumnName = "uuid")
    @JsonIgnore
    private Person translator;

}
