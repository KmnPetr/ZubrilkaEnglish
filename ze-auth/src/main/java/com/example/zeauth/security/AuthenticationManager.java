package com.example.zeauth.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AuthenticationManager implements ReactiveAuthenticationManager {


    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationManager(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        String authToken = authentication.getCredentials().toString();
        Map<String, String> claims = jwtUtil.getClaimsFromToken(authToken);
        String email = claims.get("email");

        if (email!=null && jwtUtil.validateToken(authToken)){
            List<String> role = List.of(claims.get("role"));
            List<SimpleGrantedAuthority> authorities = role.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    authorities
            );
            return  Mono.just(newAuth);
        }else return Mono.empty();
    }
}
