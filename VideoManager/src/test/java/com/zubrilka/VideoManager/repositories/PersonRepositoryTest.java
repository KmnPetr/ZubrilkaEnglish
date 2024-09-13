package com.zubrilka.VideoManager.repositories;

import com.zubrilka.VideoManager.models.Person;
import com.zubrilka.VideoManager.models.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        // Инициализация тестовых данных
        Person person1 = new Person();
        person1.setEmail("test1@example.com");
        person1.setShort_name("user1");
        person1.setPassword("password");
        person1.setRole(UserRole.ROLE_USER);
        personRepository.save(person1);

        Person person2 = new Person();
        person2.setEmail("test2@example.com");
        person2.setShort_name("user2");
        person2.setPassword("password");
        person2.setRole(UserRole.ROLE_USER);
        personRepository.save(person2);
    }

    @Test
    void testFindByEmail() {
        Person foundPerson = personRepository.findByEmail("test1@example.com");
        assertThat(foundPerson).isNotNull();
        assertThat(foundPerson.getEmail()).isEqualTo("test1@example.com");
    }

    @Test
    void testFindByName() {
        Person foundPerson = personRepository.findByName("user1");
        assertThat(foundPerson).isNotNull();
        assertThat(foundPerson.getShort_name()).isEqualTo("user1");
    }
}