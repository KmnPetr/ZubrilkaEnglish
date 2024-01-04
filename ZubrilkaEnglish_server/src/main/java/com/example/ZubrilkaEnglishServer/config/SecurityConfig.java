package com.example.ZubrilkaEnglishServer.config;

import com.example.ZubrilkaEnglishServer.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //нужно при использовании AuthProvider в провайдере сравниваются на коректность введеные логин-пароль.Тривиальная логика спринг реализует ее сам
//    private final AuthProviderImpl authProvider;
    private final PersonDetailsService personDetailsService;
    private final JWTFilter jwtFilter;
    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService, JWTFilter jwtFilter) {
        this.personDetailsService = personDetailsService;
        this.jwtFilter = jwtFilter;
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //        auth.authenticationProvider(authProvider);//нужен при использовании AuthProvider
        auth.userDetailsService(personDetailsService);
        /*.passwordEncoder(getPasswordEncoder())*/ //это взято из старой версии Spring
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{

        http
                .csrf().disable()//в rest приложении отключаем
                .authorizeHttpRequests()//настройка авторизации
                .requestMatchers(
                        "/admin",
                        "/create"
                ).hasRole("ADMIN")//к этим страничкам доступ имеет только админ
                .requestMatchers(
                        "/auth/login",
                        "auth/registration",
                        "/error",
                        "/hello",//временно сделаем ее доступной
                        "/words",
                        "/properties/get_dictionary_version",
                        "/healthcheck/**").permitAll()//на эти странички пускаем всех пользователей
                .anyRequest().hasAnyRole("USER","ADMIN")
                .and()
                .formLogin()
                .loginPage("/auth/login")//другую страничку для логина
                .loginProcessingUrl("/process_login")//сюда прийдут данные с формы можно написать любой другой адрес,на форме он тоже обозначен
                .defaultSuccessUrl("/hello",true)//url после успешной аутентификации
                .failureUrl("/auth/login?error")//в случае неуспешной аутентификации url с параметром ошибки
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login")//сюда его перенаправит после логаута
                .and()
                .sessionManagement()
                //спринг больше не будет сохранять сессию после переделки на jwt
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //подключаем JWTFilter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);



        return http.build();
    }

    /**
     * укажет каким способом шифруется пароль
     */
    @Bean
    public PasswordEncoder getPasswordEncoder(){
//        return NoOpPasswordEncoder.getInstance();//никак не шифруем пока
        return new BCryptPasswordEncoder();
    }

    @Bean
    //немного переделано
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)throws Exception{
        return authConfig.getAuthenticationManager();
    }
}
