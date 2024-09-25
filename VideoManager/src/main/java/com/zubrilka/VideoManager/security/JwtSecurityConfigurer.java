package com.zubrilka.VideoManager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtSecurityConfigurer implements SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {

    private AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    @Autowired
    public JwtSecurityConfigurer(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    public void init(HttpSecurity builder) throws Exception {

    }

    /**
     * здесь кроме всего прочего, реализована логика получения AuthenticationManager из builder.getSharedObject
     * поскольку не используя внедрение спринга его можно получить ненулевый только в этом методе
     *
     * кроме этого в этом методе происходят дополнительные настройики jwt security расширяющие настройки из файла SecurityConfig
     */
    @Override
    public void configure(HttpSecurity builder) throws Exception {

        authManager = builder.getSharedObject(AuthenticationManager.class);
        //нафиг он не нужен, cors не пропускает этот запрос чтото в заголовки не то кладется
        //UsernamePasswordAuthenticationFilter тупить начинает исключить его нельзя а запрос на POST /login идет и чтото какието ошибки падают
//        builder.addFilterBefore(new LoginAuthJwtFilter(authManager,jwtUtil), UsernamePasswordAuthenticationFilter.class);
    }

}
