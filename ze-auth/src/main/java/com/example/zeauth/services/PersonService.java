package com.example.zeauth.services;

import com.example.zeauth.controllers.validation.ValidationException;
import com.example.zeauth.models.Person;
import com.example.zeauth.models.ProfileDTO;
import com.example.zeauth.models.UserRole;
import com.example.zeauth.repositories.PersonRepository;
import jakarta.validation.constraints.AssertTrue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

@Service
@Slf4j
public class PersonService implements ReactiveUserDetailsService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return personRepository.findByEmail(username).cast(UserDetails.class);
    }

    @AssertTrue
    public Mono<ProfileDTO> registerPerson(Mono<Person> requestedPerson){
        return requestedPerson
                .flatMap(rPerson ->{
                    return personRepository
                            .existsByEmail(rPerson.getEmail())
                            .flatMap(aBoolean -> {
                                if (!aBoolean){
                                    rPerson.setRole(UserRole.ROLE_USER);
                                    rPerson.setCreated_at(new Timestamp(System.currentTimeMillis()));
                                    return personRepository.save(rPerson)
                                            .map(savedPerson ->
                                                    new ProfileDTO(
                                                            savedPerson.getEmail(),
                                                            null,
                                                            savedPerson.getShort_name()
                                            ));
                                } else {
                                    return Mono.error(new ValidationException("This email is already in use."));
                                }
                            });

                        }
        );
    }
}
