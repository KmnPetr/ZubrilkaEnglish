package com.zubrilka.VideoManager.controllers;

import com.zubrilka.VideoManager.controllers.validation.UnauthorizedException;
import com.zubrilka.VideoManager.models.PersonDto;
import com.zubrilka.VideoManager.security.jwtWeb.DefaultTokenCookieFactory;
import com.zubrilka.VideoManager.security.jwtWeb.Token;
import com.zubrilka.VideoManager.services.PersonService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * контроллер аутентификации
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PersonService personService;
    private Function<Authentication, Token> tokenCookieFactory = new DefaultTokenCookieFactory();
    private Function<Token, String> tokenStringSerializer = Objects::toString;
    @Autowired
    public AuthController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * эндпоинт логина принимает пароль и юзернейм возвращает PersonDto со всеми токенами и инфой
     * добавляет в ответ куку для дальнейшей авторизации
     */
    @PostMapping("/login")
    public ResponseEntity<PersonDto> login(@RequestBody Map<String, String> payload,
                                           HttpServletResponse response) throws UnauthorizedException, BadRequestException {
        String username = payload.get("username");
        String password = payload.get("password");

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new BadRequestException("Username and password are required.");
        }

        PersonDto personDto = personService.login(username, password);

        return new ResponseEntity<>(personDto, HttpStatus.OK);
    }
}
