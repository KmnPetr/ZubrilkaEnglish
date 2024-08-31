package com.example.zeapp.controllers;

import com.example.zeapp.models.Person;
import com.example.zeapp.models.ProfileDTO;
import com.example.zeapp.models.PropModel;
import com.example.zeapp.security.JwtUtil;
import com.example.zeapp.services.PersonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final PersonService personService;
    private final JwtUtil jwtUtil;
    private static final ResponseEntity<Object> UNAUTHORIZED = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationController(PersonService personService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.personService = personService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * выдаст новый accessToken
     * запрос должен быть произведен при помощи refreshToken обычным способом аутентификации
     */
    @GetMapping("/getAccessToken")
    public Mono<PropModel> getNewAccessToken(Mono<Principal> principal){
        return principal
                .flatMap(principal1 ->
                        personService
                                .findByUsername(principal1.getName())
                                .map(person->
                                        new PropModel(
                                                "accessToken",
                                                jwtUtil.generateAccessToken((Person) person)
                                        )
        ));
    }

    @PostMapping("/login")
    public Mono<ProfileDTO> login(ServerWebExchange swe){
        return swe.getFormData().flatMap(credentials ->{
            return personService.login(credentials.getFirst("username"),credentials.getFirst("password"));
                }
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
                            null,
                            false
                    );
                }
        ));
    }
}
