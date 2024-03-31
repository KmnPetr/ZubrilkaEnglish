package com.example.WordsManager.repositories;

import com.example.WordsManager.security.Person;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PersonRepository extends ReactiveCrudRepository<Person,Integer> {
    Mono<Person> findByEmail(String email);
}
