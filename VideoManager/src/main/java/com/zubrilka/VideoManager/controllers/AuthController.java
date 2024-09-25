package com.zubrilka.VideoManager.controllers;

import com.zubrilka.VideoManager.controllers.validation.UnauthorizedException;
import com.zubrilka.VideoManager.models.PersonDto;
import com.zubrilka.VideoManager.services.PersonService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * контроллер аутентификации
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PersonService personService;
    @Autowired
    public AuthController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * эндпоинт логина принимает пароль и юзернейм возвращает PersonDto со всеми токенами и инфой
     */
    @PostMapping("/login")
    public PersonDto login(@RequestBody Map<String, String> payload) throws UnauthorizedException, BadRequestException {
        String username = payload.get("username");
        String password = payload.get("password");

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new BadRequestException("Username and password are required.");
        }

        return personService.login(username,password);
    }
}
