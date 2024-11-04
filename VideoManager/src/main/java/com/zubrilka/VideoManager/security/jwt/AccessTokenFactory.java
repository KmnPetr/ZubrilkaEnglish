package com.zubrilka.VideoManager.security.jwt;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;

/**
 * configures access token fields
 * the token has the same uuid as its parent refresh token
 */
public class AccessTokenFactory implements Function<Token,Token> {
    private Duration tokenTtl = Duration.ofMinutes(5);


    @Override
    public Token apply(Token token) {
        Instant now = Instant.now();
        return new Token(token.uuid(),
                token.subject(),
                token.authorities().stream()
                        .filter(it->it.startsWith("GRANT_"))
                        .map(it->it.substring(0,6))
                        .toList(),
                now,
                now.plus(tokenTtl)
                );
    }
}
