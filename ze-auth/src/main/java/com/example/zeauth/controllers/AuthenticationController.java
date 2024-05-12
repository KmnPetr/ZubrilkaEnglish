package com.example.zeauth.controllers;

import com.example.zeauth.models.Person;
import com.example.zeauth.models.ProfileDTO;
import com.example.zeauth.security.JwtUtil;
import com.example.zeauth.services.PersonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final PersonService personService;
    private final JwtUtil jwtUtil;
    private static final ResponseEntity<Object> UNAUTHORIZED = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    @Autowired
    public AuthenticationController(PersonService personService, JwtUtil jwtUtil) {
        this.personService = personService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity> login(ServerWebExchange swe){
        return swe.getFormData().flatMap(credentials ->
                personService.findByUsername(credentials.getFirst("username"))
                        .cast(Person.class)
                        .map(person ->
                                Objects.equals(
                                        credentials.getFirst("password"),
                                        person.getPassword()
                                )
                                        ? ResponseEntity.ok(jwtUtil.generateToken(person))
                                        : UNAUTHORIZED
                        )
                        .defaultIfEmpty(UNAUTHORIZED)
                );
    }

    @AssertTrue
    @PostMapping("/registration")
    public Mono<ProfileDTO> registration(@Valid @RequestBody Mono<ProfileDTO> profileDTOmono){
        return personService.registerPerson(profileDTOmono.map(profileDTO -> {
                    return new Person(
                            null,
                            profileDTO.getEmail(),
                            profileDTO.getRequestPassword(),
                            profileDTO.getName(),
                            null,
                            null
                    );
                }
        ));
    }
}
