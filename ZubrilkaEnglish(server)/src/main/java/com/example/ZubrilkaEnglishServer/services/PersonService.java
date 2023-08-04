package com.example.ZubrilkaEnglishServer.services;

import com.example.ZubrilkaEnglishServer.models.Person;
import com.example.ZubrilkaEnglishServer.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PeopleRepository peopleRepository;
    @Autowired
    public PersonService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    /**
     * вернет человека по email или null
     */
    public Person findOneByEmail(String email){
        Optional<Person> foundPerson=peopleRepository.findByEmail(email);
        return foundPerson.orElse(null);
    }
}
