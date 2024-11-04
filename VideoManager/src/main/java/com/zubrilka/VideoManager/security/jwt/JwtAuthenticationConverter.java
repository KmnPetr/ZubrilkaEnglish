package com.zubrilka.VideoManager.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * responsible for converting the JWT token (JSON Web Token) from the request to the {@link Authentication} object.
 */
@Component
public class JwtAuthenticationConverter implements AuthenticationConverter {

    private final Function<String, Token> accessTokenDeserializer;
    private final Function<String,Token> refreshTokenDeserializer;
    @Autowired
    public JwtAuthenticationConverter(
            AccessTokenJwsDeserializer accessTokenDeserializer,
            RefreshTokenJweDeserializer refreshTokenDeserializer) {
        this.accessTokenDeserializer = accessTokenDeserializer;
        this.refreshTokenDeserializer = refreshTokenDeserializer;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization!=null&&authorization.startsWith("Bearer ")){
            String token = authorization.substring(7);
            Token accessToken = this.accessTokenDeserializer.apply(token);

            if (accessToken!=null){
                return new PreAuthenticatedAuthenticationToken(accessToken,token);
            }
        }
        return null;
    }
}
