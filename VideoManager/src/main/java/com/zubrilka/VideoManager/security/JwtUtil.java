package com.zubrilka.VideoManager.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zubrilka.VideoManager.models.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * класс занимается обслуживанием jwt токенов
 * их созданием, проверкой и др.
 */
@Component
public class JwtUtil {
//    49:50 время в видео с негром где можно взять другую зависимость обработчика токена jjwt
    @Value("${jwt_secret}")
    private String secret;
    @Value("${jwt_expiration_access}")
    private String timeExpirationAccess;
    @Value("${jwt_expiration_refresh}")
    private String timeExpirationRefresh;

    public boolean validateToken(String token) throws TokenExpiredException {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
//                    .withIssuer("ZubrilkaEnglish")
//                    .withSubject("User details")
                    .build();
            DecodedJWT jwt = verifier.verify(token);

            return true;
        } catch (JWTVerificationException exception) {
            // Token is not valid or expired
            return false;
        }
    }

    public Map<String, String> getClaimsFromToken(String token) {
        Map<String, String> claimsMap = new HashMap<>();
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            for (Map.Entry<String, Claim> entry : decodedJWT.getClaims().entrySet()) {
                String claimName = entry.getKey();
                String claimValue = entry.getValue().asString();
                claimsMap.put(claimName, claimValue);
            }
        } catch (JWTDecodeException exception) {
            // Токен не удалось декодировать
            // или не удалось получить список клеймов
            return null;
        }
        return claimsMap;
    }
    private String generateToken(Person person, String expirationTime){
        Date creationDate = new Date();
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(Long.parseLong(expirationTime)).toInstant());

        return JWT.create()
                .withClaim("uuid",person.getUuid().toString())
                .withClaim("username",person.getUsername())
                .withClaim("role", person.getRole().toString())
                .withIssuedAt(creationDate)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }



    public String generateAccessToken(Person person) {
        return generateToken(person,timeExpirationAccess);
    }
    public String generateRefreshToken(Person person) {
        return generateToken(person,timeExpirationRefresh);
    }

    /**
     * extracts the user id from the token
     */
    public long getUserIdFromToken(String jwtToken) {
        return Long.parseLong(getClaimsFromToken(jwtToken).get("personId"));
    }
}
