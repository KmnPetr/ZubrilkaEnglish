package com.zubrilka.VideoManager.security;

import com.zubrilka.VideoManager.models.Person;
import com.zubrilka.VideoManager.models.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * фильтр секьюрити проверяет на валидность токен jwt
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String jwt = authHeader.substring(7);

            if (jwtUtil.validateToken(jwt)) {

                Map<String, String> claims = jwtUtil.getClaimsFromToken(jwt);


                String username = claims.get("username");
                String role = claims.get("role");
                UUID uuid = UUID.fromString(claims.get("uuid"));

                Authentication auth = new UsernamePasswordAuthenticationToken(
                        new Person(uuid,null,username, UserRole.valueOf(role),null,null),
                        jwt,
                        Collections.singletonList(new SimpleGrantedAuthority(role)));

                SecurityContextHolder.getContext().setAuthentication(auth);

            } else {
                // Возвращаем 401 Unauthorized, если токен невалиден
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return; // Прерываем выполнение цепочки фильтров
            }
        }
        filterChain.doFilter(request, response); // Продолжаем цепочку фильтров
    }
}
