package com.example.ZubrilkaEnglishServer.services;

import com.example.ZubrilkaEnglishServer.models.Person;
import com.example.ZubrilkaEnglishServer.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class RegistrationServiсe {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;//внедрится из бина в конфиге
    @Autowired
    public RegistrationServiсe(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * метод зарегестрирует нового человека в БД
     */
    @Transactional
    public void register(Person person){
        System.out.println("Вызван метод register");

        String encodetPassword=passwordEncoder.encode(person.getPassword());
        person.setPassword(encodetPassword);
        person.setCreatedAt(LocalDateTime.now());//ставим время создания
        person.setRole("ROLE_USER");//по умолчанию ставим роль юзера

        peopleRepository.save(person);
    }
}
