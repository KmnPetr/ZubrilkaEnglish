package com.zubrilka.VideoManager.security.jwtWeb;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.function.Function;

public class TokenCookieSessionAuthenticationStrategy implements SessionAuthenticationStrategy {

    private Function<Authentication, Token> tokenCookieFactory = new DefaultTokenCookieFactory();
    private Function<Token, String> tokenStringSerializer = Objects::toString;

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request,
                                 HttpServletResponse response) throws SessionAuthenticationException {
        System.err.println("TokenCookieSessionAuthenticationStrategy");
        if (authentication instanceof UsernamePasswordAuthenticationToken) { //чтобы новый токен не создавался на любую успешную аутентификацию
            var token = this.tokenCookieFactory.apply(authentication);
            var tokenString = this.tokenStringSerializer.apply(token);

            var cookie = new Cookie("__Host-auth-token", tokenString);
            cookie.setPath("/");
            cookie.setDomain(null); //как этого требует префикс "Host"
            cookie.setSecure(true); //TODO кука будет передаваться только по защищенному соединению
            cookie.setHttpOnly(true); //чтобы только сервер имел доступ к этой куке
            cookie.setMaxAge((int) ChronoUnit.SECONDS.between(Instant.now(), token.expiresAt()));

            response.addCookie(cookie);
        }
    }

    public void setTokenStringSerializer(Function<Token, String> tokenStringSerializer) {
        this.tokenStringSerializer = tokenStringSerializer;
    }
}
