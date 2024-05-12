package com.example.zeauth.repositories;

import com.example.zeauth.models.Person;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PersonRepository extends ReactiveCrudRepository<Person,Integer> {
    Mono<Person> findByEmail(String email);
    Mono<Boolean> existsByEmail(String email);
}
