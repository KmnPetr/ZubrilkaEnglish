package com.zubrilka.VideoManager.security.jwt;

import org.springframework.security.core.Authentication;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * factory for the creation of a refresh token
 * the refresh token has the same uuid with the access tokens reproduced from it
 * has rights only to update and logout
 * stores the rest of the rights with the prefix "GRANT_"
 */
public class RefreshTokenFactory implements Function<Authentication, Token> {
    private Duration tokenTtl = Duration.ofDays(1);
    @Override
    public Token apply(Authentication authentication) {
        List<String> authorities = new LinkedList<>();
        authorities.add("JWT_REFRESH");
        authorities.add("JWT_LOGOUT");
        authentication.getAuthorities().stream()
                .map(t->t.getAuthority())
                .map(authority->"GRANT_"+authority)
                .forEach(authorities::add);

        Instant now = Instant.now();

        return new Token(UUID.randomUUID(), authentication.getName(), authorities, now, now.plus(tokenTtl));
    }
}
