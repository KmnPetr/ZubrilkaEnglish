package com.example.zeauth.security;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
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
        String userName;
        try {
            userName = jwtUtil.extractUsername(authToken);
        } catch (Exception e){
            userName = null;
            log.info(String.valueOf(e));
        }

        if (userName!=null && jwtUtil.validateToken(authToken)){
            Claims claims = jwtUtil.getClaimsFromToken(authToken);
            List<String> role = claims.get("role",List.class);
            List<SimpleGrantedAuthority> authorities = role.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                    userName,
                    null,
                    authorities
            );
            return  Mono.just(newAuth);
        }else return Mono.empty();
    }
}
