package com.zubrilka.VideoManager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zubrilka.VideoManager.models.converters.PhraseListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import java.util.List;
import java.util.UUID;

/**
 * the object stores the translation of the video.
 * The basic information is contained in the list of phrases
 */
@Entity
@Table(name = "translation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Translation {
    @Id
    @Column(name = "uuid", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(name = "video_info_uuid")
    private UUID videoInfoUuid;

    @Column(name = "version")
    private Long version;

    @Convert(converter = PhraseListConverter.class)
    @Column(name = "phrases")
    private List<Phrase> phrases;
}
