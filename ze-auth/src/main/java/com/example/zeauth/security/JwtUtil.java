package com.example.zeauth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.zeauth.models.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtil {
    @Value("${jwt_secret}")
    private String secret = "Secret";
    @Value("${jwt_expiration_access}")
    private String timeExpirationAccess;
    @Value("${jwt_expiration_refresh}")
    private String timeExpirationRefresh;

//    public String extractUsername(String authToken) {
//            return getClaimsFromToken(authToken)
//                    .getSubject();
//    }


    public boolean validateToken2(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
//                    .withIssuer("ZubrilkaEnglish")
//                    .withSubject("User details")
                    .build();
            DecodedJWT jwt = verifier.verify(token);

            return true;
        } catch (JWTVerificationException exception) {
            // Token is not valid
            return false;
        }
    }
    private String generateToken2(Person person, String expirationTime){
        Date creationDate = new Date();
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(Long.parseLong(expirationTime)).toInstant());

        return JWT.create()
//                .withSubject("User details")
//                .withIssuer("ZubrilkaEnglish")
                .withClaim("email",person.getEmail())
                .withClaim("role", person.getRole().toString())
                .withIssuedAt(creationDate)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public Map<String, String> getClaimsFromToken2(String token) {
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


//    public boolean validateToken(String authToken) {
//        return getClaimsFromToken(authToken)
//                .getExpiration()
//                .before(new Date());
//    }



//    public Claims getClaimsFromToken(String authToken) {
//        String key = Base64.getEncoder().encodeToString(secret.getBytes());
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJwt(authToken)
//                .getBody();
//    }

    public String generateAccessToken(Person person) {
        return generateToken2(person,timeExpirationAccess);
    }
    public String generateRefreshToken(Person person) {
        return generateToken2(person,timeExpirationRefresh);
    }


//    private String generateToken(Person person, String expirationTime){
//
//        long expirationSeconds = Long.parseLong(expirationTime);
//        Date creationDate = new Date();
//        Date expirationDate = new Date(creationDate.getTime() + expirationSeconds * 1000);
//
//        HashMap<String,Object> claims = new HashMap<>();
//        claims.put("role", List.of(person.getRole()));
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(person.getUsername())
//                .setIssuedAt(creationDate)
//                .setExpiration(expirationDate)
//                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
//                .compact();
//
//    }
}
