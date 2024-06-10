package com.example.zeapp.repositories;

import com.example.zeapp.models.Person;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface PersonRepository extends ReactiveCrudRepository<Person,Integer> {
    Mono<Person> findByEmail(String email);
    Mono<Boolean> existsByEmail(String email);

    default Mono<Person> updateUserName(Long id, String newName) {
        return findById(Math.toIntExact(id))
                .map(person -> {
                    person.setShort_name(newName);
                    return person;
                })
                .flatMap(this::save);
    }

    default Mono<Person> updateUserEmail(long id, String newEmail) {
        return findById(Math.toIntExact(id))
                .map(person -> {
                    person.setEmail(newEmail);
                    return person;
                })
                .flatMap(this::save);
    }
}
