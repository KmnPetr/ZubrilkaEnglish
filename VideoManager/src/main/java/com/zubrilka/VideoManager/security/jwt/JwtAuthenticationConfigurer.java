package com.zubrilka.VideoManager.security.jwt;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;


@Component
public class JwtAuthenticationConfigurer implements SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity>  {

    private final JdbcTemplate jdbcTemplate;
    private final JwtLogoutFilter jwtLogoutFilter;
    private final RefreshTokenFilter refreshTokenFilter;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final RequestJwtTokensFilter requestJwtTokensFilter;
    private final TokenAuthenticationUserDetailsService tokenAuthenticationUserDetailsService;
    @Autowired
    public JwtAuthenticationConfigurer(JdbcTemplate jdbcTemplate,
                                       JwtLogoutFilter jwtLogoutFilter,
                                       RefreshTokenFilter refreshTokenFilter,
                                       JwtAuthenticationConverter jwtAuthenticationConverter,
                                       RequestJwtTokensFilter requestJwtTokensFilter,
                                       TokenAuthenticationUserDetailsService tokenAuthenticationUserDetailsService) {
        this.jdbcTemplate = jdbcTemplate;
        this.jwtLogoutFilter = jwtLogoutFilter;
        this.refreshTokenFilter = refreshTokenFilter;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
        this.requestJwtTokensFilter = requestJwtTokensFilter;
        this.tokenAuthenticationUserDetailsService = tokenAuthenticationUserDetailsService;
    }

    @Override
    public void init(HttpSecurity builder) throws Exception {
        CsrfConfigurer csrfConfigurer = builder.getConfigurer(CsrfConfigurer.class);
        if (csrfConfigurer != null) {
            csrfConfigurer.ignoringRequestMatchers(new AntPathRequestMatcher("/jwt/tokens", "POST"));
        }
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {

        var jwtAuthenticationFilter = new AuthenticationFilter(builder.getSharedObject(AuthenticationManager.class), jwtAuthenticationConverter);
        jwtAuthenticationFilter.setSuccessHandler((request, response, authentication) -> CsrfFilter.skipRequest(request));
        jwtAuthenticationFilter.setFailureHandler((request, response, exception) -> response.sendError(HttpServletResponse.SC_FORBIDDEN));

        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(tokenAuthenticationUserDetailsService);


        builder.addFilterAfter(requestJwtTokensFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, CsrfFilter.class)
                .addFilterAfter(refreshTokenFilter, ExceptionTranslationFilter.class)
                .addFilterAfter(jwtLogoutFilter, ExceptionTranslationFilter.class)
                .authenticationProvider(authenticationProvider);
    }
}
