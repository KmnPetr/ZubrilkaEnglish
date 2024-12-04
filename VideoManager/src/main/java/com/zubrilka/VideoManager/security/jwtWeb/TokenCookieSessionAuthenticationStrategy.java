package com.zubrilka.VideoManager.security.jwtWeb;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

@Component
public class TokenCookieSessionAuthenticationStrategy implements SessionAuthenticationStrategy {

    private Function<Authentication, Token> tokenCookieFactory = new DefaultTokenCookieFactory();
    private final Function<Token, String> tokenStringSerializer;
    private String profile;

    @Value("${cookie-name}")
    String cookieName;

    @Autowired
    public TokenCookieSessionAuthenticationStrategy(TokenCookieJweStringSerializer tokenCookieJweStringSerializer) {
        this.tokenStringSerializer = tokenCookieJweStringSerializer;
    }

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request,
                                 HttpServletResponse response) throws SessionAuthenticationException {
        System.err.println("TokenCookieSessionAuthenticationStrategy");
        if (authentication instanceof UsernamePasswordAuthenticationToken) { //чтобы новый токен не создавался на любую успешную аутентификацию
            var token = this.tokenCookieFactory.apply(authentication);
            var tokenString = this.tokenStringSerializer.apply(token);

            var cookie = new Cookie(cookieName, tokenString);
            cookie.setPath("/");
            cookie.setDomain(null); //как этого требует префикс "Host"
            cookie.setSecure(true);
            cookie.setHttpOnly(true); //чтобы только сервер имел доступ к этой куке
            cookie.setMaxAge((int) ChronoUnit.SECONDS.between(Instant.now(), token.expiresAt()));

            response.addCookie(cookie);
        }
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
