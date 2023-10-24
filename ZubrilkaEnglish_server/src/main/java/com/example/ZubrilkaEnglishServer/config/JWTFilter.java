package com.example.ZubrilkaEnglishServer.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.ZubrilkaEnglishServer.security.JWTUtil;
import com.example.ZubrilkaEnglishServer.services.PersonDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final PersonDetailsService personDetailsService;
    @Autowired
    public JWTFilter(JWTUtil jwtUtil, PersonDetailsService personDetailsService) {
        this.jwtUtil = jwtUtil;
        this.personDetailsService = personDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader=request.getHeader("Authorization");//возьмет данные из этого заголовка из пришедшего запроса
        if (authHeader!=null&&!authHeader.isBlank()&&authHeader.startsWith("Bearer ")){

            String jwt=authHeader.substring(7/*"Bearer " пропускаем*/);

            if (jwt.isBlank()){

                //если пустой, то отсылаем ошибку
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid JWT token in Bearer header");
            }else {

                try {
                    String email=jwtUtil.validateTokenAndRetrieveClaim(jwt);

                    //получим данные из БД по email
                    UserDetails userDetails=personDetailsService.loadUserByUsername(email);


                    //здесь проходит авторизация пользователя, Этот authToken содержит: Пример:
                    // UsernamePasswordAuthenticationToken [Principal=com.example.ZubrilkaEnglishServer.security.PersonDetails@35b2bef4, Credentials=[PROTECTED], Authenticated=true, Details=null, Granted Authorities=[ROLE_USER]]
                    UsernamePasswordAuthenticationToken authToken=
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    userDetails.getPassword(),
                                    userDetails.getAuthorities());


//                    если authToken нет в security context, то мы его туда кладем, без нее не работает
                    if (SecurityContextHolder.getContext().getAuthentication()==null){
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }catch (JWTVerificationException e){
                    //если подпись неправильная или срок годности истек или еще чтото
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid JWT token");
                }
            }
        }
        //фильтр перехватил, обработал и продвинул запрос дальше
        filterChain.doFilter(request,response);

    }
}
