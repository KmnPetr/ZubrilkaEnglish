package com.example.zeapp.repositories;

import com.example.zeapp.models.Person;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PersonRepository extends ReactiveCrudRepository<Person,Long> {
    Mono<Person> findByEmail(String email);
    Mono<Boolean> existsByEmail(String email);

    default Mono<Person> updateUserName(Long id, String newName) {
        return findById(id)
                .map(person -> {
                    person.setShort_name(newName);
                    return person;
                })
                .flatMap(this::save);
    }

    default Mono<Person> updateUserEmail(long id, String newEmail) {
        return findById(id)
                .map(person -> {
                    person.setEmail(newEmail);
                    return person;
                })
                .flatMap(this::save);
    }

    default Mono<Person> updateUserPassword(long id, String password) {
        return findById(id)
                .map(person -> {
                    person.setPassword(password);
                    person.setIsTempProf(false);
                    return person;
                })
                .flatMap(this::save);
    }
}
