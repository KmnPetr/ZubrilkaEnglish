package com.zubrilka.VideoManager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * основная конфигурация секьюрити
 */
@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(JwtAuthenticationProvider jwtAuthenticationProvider, UserDetailsService userDetailsService) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.userDetailsService = userDetailsService;
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(daoAuthenticationProvider(), jwtAuthenticationProvider));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(sessionManagement-> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//отключает сохранение сессии
                .httpBasic(withDefaults()) // Включаем базовую аутентификацию
                .authorizeHttpRequests(authorize ->
                        authorize.anyRequest().permitAll()
//                        authorize
//                                .requestMatchers(
//                                        "/test/hello",
//                                        "test-route/hello"
//                                ).hasAnyAuthority(UserRole.ROLE_ADMIN.name(),UserRole.ROLE_USER.name())
//                                .anyRequest().denyAll()
                )
//                .exceptionHandling(exceptionHandling ->
//                        exceptionHandling
//                                .authenticationEntryPoint(customAuthenticationEntryPoint()) // Точка входа для перенаправления
//                )
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .rememberMe(Customizer.withDefaults())

                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }


//    // Кастомная точка входа вызывается в случае проваленной аутентификации
//    //при переопределении интерфейса AuthenticationEntryPoint можно более гипко настроить ответ пользователю
//    private AuthenticationEntryPoint customAuthenticationEntryPoint() {
//        return new LoginUrlAuthenticationEntryPoint("/custom-login");
//    }
}
