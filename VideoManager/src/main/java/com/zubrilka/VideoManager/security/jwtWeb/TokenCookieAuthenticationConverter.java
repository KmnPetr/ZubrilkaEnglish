package com.zubrilka.VideoManager.security.jwtWeb;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.function.Function;
import java.util.stream.Stream;

public class TokenCookieAuthenticationConverter implements AuthenticationConverter {

    private final Function<String, Token> tokenCookieStringDeserializer;
    String cookieName;

    public TokenCookieAuthenticationConverter(Function<String, Token> tokenCookieStringDeserializer, String cookieName) {
        this.tokenCookieStringDeserializer = tokenCookieStringDeserializer;
        this.cookieName = cookieName;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        System.err.println("TokenCookieAuthenticationConverter");
        if (request.getCookies() != null) {
            return Stream.of(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(cookieName))
                    .findFirst()
                    .map(cookie -> {
                        var token = this.tokenCookieStringDeserializer.apply(cookie.getValue());
                        return new PreAuthenticatedAuthenticationToken(token, cookie.getValue());
                    })
                    .orElse(null);
        }

        return null;
    }
}
