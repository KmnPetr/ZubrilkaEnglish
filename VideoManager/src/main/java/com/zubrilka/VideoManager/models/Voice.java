package com.zubrilka.VideoManager.models;

import com.zubrilka.VideoManager.enums.Sex;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * хранит ссылки на обьекты voice
 */
@Entity
@Table(name = "voice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Voice {
    @Id
    @Column(name = "uuid", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    @Column(name = "text", nullable = false)
    private String text; //примерный озвученный текст
    @Column(name = "voice", nullable = false)
    private String voice; //имя человека или ai-актера озвучившего текст
    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private Sex sex; //пол голоса

    @Column(name = "local_link", nullable = false)
    private String local_link; //ссылка на файл в локальном хранилище сервера
}
