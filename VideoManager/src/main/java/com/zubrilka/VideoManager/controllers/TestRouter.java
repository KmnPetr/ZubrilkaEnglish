package com.zubrilka.VideoManager.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Map;

/**
 * тестирует роутер функции
 */
@Configuration
public class TestRouter {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(){
        return RouterFunctions.route()
                .GET("/api/test-route/hello",request -> {
                    UserDetails userDetails = request.principal().map(Authentication.class::cast)
                            .map(Authentication::getPrincipal)
                            .map(UserDetails.class::cast)
                            .orElseThrow();
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(Map.of(
                                    "greeting", "Hello1, %s!".formatted(userDetails)
                            ));

                }).build();
    }
}
