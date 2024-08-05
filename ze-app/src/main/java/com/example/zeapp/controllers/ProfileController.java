package com.example.zeapp.controllers;

import com.example.zeapp.models.ProfileDTO;
import com.example.zeapp.models.PropModel;
import com.example.zeapp.security.JwtUtil;
import com.example.zeapp.services.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/profile")
@Slf4j
public class ProfileController {
    private final PersonService personService;
    private final JwtUtil jwtUtil;
    @Autowired
    public ProfileController(PersonService personService, JwtUtil jwtUtil) {
        this.personService = personService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * выдаст нового пользователя со случайно сгенерированным именем
     */
    @GetMapping("/getTemporaryProfile")
    public Mono<ProfileDTO> getTemporaryProfile(){
        return personService.getTemporaryProfile();
    }
    /**
     * the method updates information on some user fields such as email, name or other
     * it accepts PropModel json
     * in the "key" field - the name of the field to replace
     * in the "value" field - the new desired value
     */
    @PatchMapping("/update-field")
    public Mono<ProfileDTO> updateProfileField(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Mono<PropModel> fieldInfo) {
        String jwtToken;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
        } else jwtToken = null;

        return fieldInfo
                .flatMap(propModel->
                        personService
                                .changeFieldOfUsersProfile(
                                        jwtUtil.getUserIdFromToken(jwtToken),
                                        propModel.getKey(),
                                        propModel.getValue()
                                )
                );
    }
}
