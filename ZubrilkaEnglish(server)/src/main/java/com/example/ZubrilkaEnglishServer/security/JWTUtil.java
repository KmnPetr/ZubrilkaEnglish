package com.example.ZubrilkaEnglishServer.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * здесь находится генерация и валидация токена
 */
@Component
public class JWTUtil {
    @Value("${jwt_secret}")
    private String secret;
    public String generateToken(String email){

        Date expirationDate=Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());//дата окончания срока годности токена

        return JWT.create()
                .withSubject("User details")//что вообще хранится в этом токене
                .withClaim("email",email)//ключ-значение таких полей может быть несколько
                .withIssuedAt(new Date())//время выдачи токена
                .withIssuer("ZubrilkaEnglish")//кем выдан
                .withExpiresAt(expirationDate)//когда истекает срок годности
                .sign(Algorithm.HMAC256(secret));//алгоритм шифрования
    }

    /**
     * проверяет токен и извлекает username
     */
    public String validateTokenAndRetrieveClaim(String token)throws JWTVerificationException {
        JWTVerifier verifier=JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("ZubrilkaEnglish")//проверит через секрет валидность сабджекта и ищщура
                .build();

        DecodedJWT jwt=verifier.verify(token);//верифицируем и извлекаем данные из токена
        return jwt.getClaim("email").asString();//вернули email
    }
}
