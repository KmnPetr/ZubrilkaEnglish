package com.example.WordsManager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig{
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> {
                    auth.pathMatchers(
                            "/listWords.html", //TODO вернуть потом запретить всем
                            "/fillTable.js", //TODO вернуть потом запретить всем
                            "/requestingListWords.js", //TODO вернуть потом запретить всем
                            "mainPage.html",//TODO вернуть потом запретить всем
                            "/styles.css",
                            "/words/**", //TODO вернуть потом запретить всем
                            "/backup/**" //TODO вернуть потом запретить всем
                    ).permitAll();
//                    auth.pathMatchers(
//                    ).hasRole("ADMIN");
//                    auth.anyExchange().authenticated();
                    auth.anyExchange().permitAll(); //TODO  ээээээээээ
                })
                .httpBasic(withDefaults())
                .formLogin(withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
