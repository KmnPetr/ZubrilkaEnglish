package com.zubrilka.VideoManager.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * JWT token-based authentication provider
 * not implemented yet
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Since there is no authentication support, we throw an exception
        throw new UnsupportedOperationException("JwtAuthenticationProvider does not support authentication.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // We return false to indicate that this provider does not support any authentication classes.
        return false;
    }
}
