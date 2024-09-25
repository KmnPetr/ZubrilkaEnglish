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
        person1.setUsername("user1");
        person1.setPassword("password");
        person1.setRole(UserRole.ROLE_TRANSLATOR);
        personRepository.save(person1);

        Person person2 = new Person();
        person2.setUsername("user2");
        person2.setPassword("password");
        person2.setRole(UserRole.ROLE_TRANSLATOR);
        personRepository.save(person2);
    }

    @Test
    void testFindByUsername() {
        Person foundPerson = personRepository.findByUsername("user1").orElse(null);
        assertThat(foundPerson).isNotNull();
        assertThat(foundPerson.getUsername()).isEqualTo("user1");
    }

}