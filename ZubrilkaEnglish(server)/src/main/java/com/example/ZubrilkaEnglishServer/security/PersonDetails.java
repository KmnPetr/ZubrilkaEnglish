package com.example.ZubrilkaEnglishServer.security;

import com.example.ZubrilkaEnglishServer.models.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class PersonDetails implements UserDetails {
    private final Person person;

    public PersonDetails(Person person) {this.person = person;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));//коллекция прав пользователя
    }

    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    /**
     * вместо имени пользователи будут атентифицироваться по email
     */
    @Override
    public String getUsername() {
        return this.person.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;//не просрочен
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;//не заблочен
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;//пароль не просрочен
    }

    @Override
    public boolean isEnabled() {
        return true;//аккаунт включен и работает
    }

    /**
     * нужен чтобы получать данные аутентифицированного пользователя
     */
    public Person getPerson(){return this.person;}
}
