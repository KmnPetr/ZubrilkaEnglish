package com.example.zeauth.services;

import com.example.zeauth.controllers.validation.UnauthorizedException;
import com.example.zeauth.controllers.validation.ValidationException;
import com.example.zeauth.models.Person;
import com.example.zeauth.models.ProfileDTO;
import com.example.zeauth.models.UserRole;
import com.example.zeauth.repositories.PersonRepository;
import com.example.zeauth.security.JwtUtil;
import jakarta.validation.constraints.AssertTrue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

@Service
@Slf4j
public class PersonService implements ReactiveUserDetailsService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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
                                    rPerson.setPassword(passwordEncoder.encode(rPerson.getPassword()));
                                    return personRepository.save(rPerson)
                                            .map(this::convertToProfileDTO);
                                } else {
                                    return Mono.error(new ValidationException("This email is already in use."));
                                }
                            });

                        }
        );
    }

    /**
     * аутентифицирует пользователя,
     * выдаст токены или ошибки
     */
    public Mono<ProfileDTO> login(String username, String password) {

        return findByUsername(username)
                .cast(Person.class)
                .flatMap(person ->{
                            if (passwordEncoder.matches(password,person.getPassword())){
                                return Mono.just(convertToProfileDTO(person));
                            }else {
                                return Mono.error(new UnauthorizedException("Invalid password."));
                            }
                        }
                )
                .switchIfEmpty(Mono.error(new UnauthorizedException("User not found.")));
    }
    //конвертирует Person в ProfileDTO
    private ProfileDTO convertToProfileDTO(Person person){
        return new ProfileDTO(
                person.getEmail(),
                null,
                person.getShort_name(),
                jwtUtil.generateAccessToken(person),
                jwtUtil.generateRefreshToken(person),
                person.getCreated_at()
        );
    }
}
