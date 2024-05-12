package com.example.zeauth.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @Autowired
    public SecurityConfig(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity){
        return httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange(auth->{

                    auth.pathMatchers(
                            "/",
                            "/login",
                            "/healthcheck",
                            "/test-auth/guest",
                            "/auth/registration"
                    ).permitAll();

                    auth.pathMatchers(
                            "/test-auth/user"
                    ).hasAnyRole("USER","ADMIN");

                    auth.pathMatchers(
                            "/test-auth/admin"
                    ).hasRole("ADMIN");

//                    auth.anyExchange()
//                            .authenticated();
                })
                .exceptionHandling(it ->{
                            it.authenticationEntryPoint((swe,e) ->
                                    Mono.fromRunnable(
                                            () -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)
                                    )

                            );
                            it.accessDeniedHandler((swe,e) ->
                                    Mono.fromRunnable(
                                            () -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN)
                                    )
                            );
                        }
                        )
                .httpBasic(withDefaults())
                .formLogin(withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();  //TODO
    }
}
