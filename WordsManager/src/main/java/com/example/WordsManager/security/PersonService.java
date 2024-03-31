package com.example.WordsManager.security;

import com.example.WordsManager.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PersonService implements ReactiveUserDetailsService {
    private final PersonRepository personRepository;
    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return personRepository
                .findByEmail(email)
                .cast(UserDetails.class);
    }
}
