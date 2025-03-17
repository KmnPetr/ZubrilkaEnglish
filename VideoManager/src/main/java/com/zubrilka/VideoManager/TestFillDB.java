package com.zubrilka.VideoManager;

import com.zubrilka.VideoManager.enums.Sex;
import com.zubrilka.VideoManager.enums.UserRole;
import com.zubrilka.VideoManager.models.*;
import com.zubrilka.VideoManager.repositories.PersonRepository;
import com.zubrilka.VideoManager.repositories.TranslationRepository;
import com.zubrilka.VideoManager.repositories.VideoInfoRepository;
import com.zubrilka.VideoManager.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;

/**
 * the class runs the initialization method during a test run of the server under the "local" profile
 * be careful
 * * you should not run the server on a prod with this class with the "local" profile
 */
@SpringBootApplication
class TestFillDB {


    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final VideoInfoRepository videoInfoRepository;
    private final TranslationRepository translationRepository;
    private final VideoRepository videoRepository;

    @Autowired
    public TestFillDB(PersonRepository personRepository, PasswordEncoder passwordEncoder, VideoInfoRepository videoInfoRepository, TranslationRepository translationRepository, VideoRepository videoRepository) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.videoInfoRepository = videoInfoRepository;
        this.translationRepository = translationRepository;
        this.videoRepository = videoRepository;
    }

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active","local");
        SpringApplication application = new SpringApplication(TestFillDB.class);
//        application.setWebApplicationType(WebApplicationType.NONE);
        ApplicationContext context = application.run(args);

        TestFillDB testFillDB = context.getBean(TestFillDB.class);
        testFillDB.init();

        SpringApplication.exit(context);
    }

    public void init() {
        System.err.println("Warning!!! init: TestFillDataBase");


        Person adminPerson = new Person(
                null,
                passwordEncoder.encode("111"),
                "111",
                "Petr",
                Sex.MALE,
                UserRole.ROLE_ADMIN,
                new Timestamp(System.currentTimeMillis()),
                null,
                null);

        Person savedPerson = personRepository.save(adminPerson);
        System.out.println("Person uuid: %s".formatted(savedPerson.getUuid()));
    }
}