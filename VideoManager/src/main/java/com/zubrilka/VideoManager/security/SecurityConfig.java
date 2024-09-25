package com.zubrilka.VideoManager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * основная конфигурация секьюрити
 */
@Configuration
//@EnableWebSecurity(debug = false)
//@CrossOrigin(origins = "http://localhost:3000")
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final JwtSecurityConfigurer jwtSecurityConfigurer;

    //Точка входа, вызывается в случае если нужно вернуть ошибку
    //напр. authEntryPoint.comence(..param..)
    private AuthenticationEntryPoint authEntryPoint = ((request, response, authException) -> {
        response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"Access to protected resource\"");
        response.sendError(HttpStatus.UNAUTHORIZED.value(),"Error message");
    });

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, JwtSecurityConfigurer jwtSecurityConfigurer) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.jwtSecurityConfigurer = jwtSecurityConfigurer;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors->corsConfigurer())
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
//                        // Точка входа для перенаправления в случае возникновения ошибок
//                        exceptionHandling.authenticationEntryPoint(authEntryPoint)
//                )
//                .httpBasic(withDefaults()) // Включаем базовую аутентификацию
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .apply(jwtSecurityConfigurer);


        return httpSecurity.build();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance();
    }


//    // Кастомная точка входа вызывается в случае проваленной аутентификации
//    //при переопределении интерфейса AuthenticationEntryPoint можно более гипко настроить ответ пользователю
//    private AuthenticationEntryPoint customAuthenticationEntryPoint() {
//        return new LoginUrlAuthenticationEntryPoint("/custom-login");
//    }


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
