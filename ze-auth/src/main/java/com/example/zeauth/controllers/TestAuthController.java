package com.example.zeauth.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/test-auth")
public class TestAuthController {
    @GetMapping("/guest")
    public Mono<String> testGuest(){
        return Mono.just("Hello, guest!!");
    }
    @GetMapping("/user")
    public Mono<String> testUser(Mono<Principal> principal) {
        return principal
                .map(Principal::getName)
                .map(name -> String.format("Hello, %s", name));
//        return Mono.just("Hello, user!!");
    }
    @GetMapping("/admin")
    public Mono<String> testAdmin(Mono<Principal> principal) {
        return principal
                .map(Principal::getName)
                .map(name -> String.format("Admin access: %s", name));
    }
}
