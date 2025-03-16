package com.zubrilka.VideoManager.security;

import com.zubrilka.VideoManager.security.jwtWeb.GetCsrfTokenFilter;
import com.zubrilka.VideoManager.security.jwtWeb.TokenCookieAuthenticationConfigurer;
import com.zubrilka.VideoManager.security.jwtWeb.TokenCookieSessionAuthenticationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Основная конфигурация секьюрити
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Value("${spring.profiles.active:default}") // Получение текущего профиля
    private String profile;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer,
            TokenCookieSessionAuthenticationStrategy tokenCookieSessionAuthenticationStrategy,
            CustAuthSuccessHandler successHandler
    ) throws Exception {

        if ("local".equals(profile)){
            tokenCookieSessionAuthenticationStrategy.setProfile(profile);
        }

        httpSecurity
                .apply(tokenCookieAuthenticationConfigurer);
        httpSecurity
                .httpBasic(Customizer.withDefaults())
//                .formLogin(Customizer.withDefaults())
                .formLogin(fl ->
                        fl.loginProcessingUrl("/api/login")
                                .successHandler(successHandler)
                                .permitAll()
                    )
                .addFilterAfter(new GetCsrfTokenFilter(), ExceptionTranslationFilter.class) //csrf token receive filter
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(
                                        "/api/test/admin"
                                ).hasRole("ADMIN")
                                .requestMatchers(
                                        "/api/test/hello",
                                        "/api/test/user",
                                        "/api/video/**",
                                        "/api/video-info",
                                        "/api/video-info/**",
                                        "/api/translation/**",
                                        "/api/icon/**",
                                        "/api/audio/**",
                                        "/api/voice/**",
                                        "/api/person/rating_voices",
                                        "/api/person/rating_get"
                                ).hasAnyRole("ADMIN","TRANSLATOR")
                                .requestMatchers(
                                        "/api/test/free",
                                        "/api/error",
                                        "/api/auth/login",
                                        "/api/login",
                                        "/index.html",
                                        "/",
                                        "/vite.svg",
                                        "/assets/**",
                                        "/favicon.ico"
                                ).permitAll()
                                /*.anyRequest().authenticated()*/)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) //this is my class
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .sessionAuthenticationStrategy(tokenCookieSessionAuthenticationStrategy))
//                .csrf(csrf -> csrf.csrfTokenRepository(new CookieCsrfTokenRepository()) //спринг по умолчанию использует сессии, поэтому нужно поменять на CookieCsrfTokenRepository
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()) //по умолчанию стоит что-то другое, подробнее надо узнавать про дополнительные алгоритмы офускации scrf токена, а в данном примере он будет передаваться клиенту в чистом виде
//                        .sessionAuthenticationStrategy((authentication, request, response) -> {})) //нужно чтобы при каждой новой куки аутентификации не создавался новый scrf токен
                .csrf(csrf->csrf.disable())
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
