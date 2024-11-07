package com.zubrilka.VideoManager.security;

import com.zubrilka.VideoManager.security.jwtWeb.GetCsrfTokenFilter;
import com.zubrilka.VideoManager.security.jwtWeb.TokenCookieAuthenticationConfigurer;
import com.zubrilka.VideoManager.security.jwtWeb.TokenCookieJweStringSerializer;
import com.zubrilka.VideoManager.security.jwtWeb.TokenCookieSessionAuthenticationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Основная конфигурация секьюрити
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            TokenCookieJweStringSerializer tokenCookieJweStringSerializer,
            TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer
    ) throws Exception {

        var tokenCookieSessionAuthenticationStrategy = new TokenCookieSessionAuthenticationStrategy();
        tokenCookieSessionAuthenticationStrategy.setTokenStringSerializer(tokenCookieJweStringSerializer);

        httpSecurity.apply(tokenCookieAuthenticationConfigurer);
        httpSecurity.httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .addFilterAfter(new GetCsrfTokenFilter(), ExceptionTranslationFilter.class) //csrf token receive filter
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(
                                        "/manager.html", "/manager",
                                        "/ppp.html","/ppp"
                                ).hasRole("MANAGER")
                                .requestMatchers(
                                        "/error",
                                        "/auth/login",
                                        "/video/list-video"
                                ).permitAll()
                                .anyRequest().authenticated())
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

    // Настройка CORS
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")  // Разрешаем запросы с фронтенда
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Указываем допустимые методы
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .exposedHeaders("UUID", "X-Filename", "Authorization", "Content-Type", "Location", "X-Total-Count","Access-Control-Allow-Origin");
            }
        };
    }
}
