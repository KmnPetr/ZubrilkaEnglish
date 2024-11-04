package com.zubrilka.VideoManager.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class Video {

    @Id
    @Column(name = "uuid", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "bytes", nullable = false)
    private byte[] bytes;
}
