package com.zubrilka.VideoManager.util;

import com.zubrilka.VideoManager.models.*;
import com.zubrilka.VideoManager.repositories.PersonRepository;
import com.zubrilka.VideoManager.repositories.TranslationRepository;
import com.zubrilka.VideoManager.repositories.VideoInfoRepository;
import com.zubrilka.VideoManager.repositories.VideoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * the class runs the initialization method during a test run of the server under the "local" profile
 * be careful
 * * you should not run the server on a prod with this class with the "local" profile
 */
@Component
@Profile("local")
public class TestFillDataBase {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final VideoInfoRepository videoInfoRepository;
    private final TranslationRepository translationRepository;
    private final VideoRepository videoRepository;
    @Autowired
    public TestFillDataBase(PersonRepository personRepository, PasswordEncoder passwordEncoder, VideoInfoRepository videoInfoRepository, TranslationRepository translationRepository, VideoRepository videoRepository) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.videoInfoRepository = videoInfoRepository;
        this.translationRepository = translationRepository;
        this.videoRepository = videoRepository;
    }

    @PostConstruct
    public void init() {
        System.err.println("Warning!!! init: TestFillDataBase");

        personRepository.deleteAll();
        videoInfoRepository.deleteAll();
        translationRepository.deleteAll();
        videoRepository.deleteAll();

        Person adminPerson = new Person(null,passwordEncoder.encode("111"),"111", UserRole.ROLE_ADMIN,new Timestamp(System.currentTimeMillis()),null);

        Person savedPerson = personRepository.save(adminPerson);
        System.out.println("Person uuid: %s".formatted(savedPerson.getUuid()));

        Translation translation = new Translation(
                null,
                0L,
                List.of(
                        new Phrase(4L,
                                "这是一个测试短语...",
                                "This is a test phrase...",
                                "Это тестовая фраза...",
                                6000L,
                                7000L),
                        new Phrase(5L,
                                "一个非常大的短语。一个非常大的短语。一个非常大的短语。一个非常大的短语。一个非常大的短语。一个非常大的短语。一个非常大的短语。一个非常大的短语。一个非常大的短语。一个非常大的短语。",
                                "A very big phrase. A very big phrase. A very big phrase. A very big phrase. A very big phrase. A very big phrase. A very big phrase. A very big phrase. A very big phrase. A very big phrase. ",
                                "Очень большая фраза. Очень большая фраза. Очень большая фраза. Очень большая фраза. Очень большая фраза. Очень большая фраза. Очень большая фраза. Очень большая фраза. Очень большая фраза. Очень большая фраза. ",
                                8000L,
                                9000L)
                )
        );

        Translation savedTranslation = translationRepository.save(translation);

        List<VideoInfo> videoInfoList = List.of(
                new VideoInfo(null,"testVideo_1",savedPerson.getUuid(), savedPerson.getUsername(), null,null,savedPerson,savedTranslation,null),
                new VideoInfo(null,"testVideo_2",savedPerson.getUuid(), savedPerson.getUsername(), null, null,savedPerson,null,null),
                new VideoInfo(null,"testVideo_3",savedPerson.getUuid(), savedPerson.getUsername(), null, null,savedPerson,null,null),
                new VideoInfo(null,"testVideo_4",savedPerson.getUuid(), savedPerson.getUsername(), null, null,savedPerson,null,null)
        );



        videoInfoRepository.saveAll(videoInfoList);
    }
}
