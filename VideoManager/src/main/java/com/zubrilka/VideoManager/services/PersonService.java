package com.zubrilka.VideoManager.services;

import com.zubrilka.VideoManager.controllers.validation.UnauthorizedException;
import com.zubrilka.VideoManager.models.Person;
import com.zubrilka.VideoManager.models.PersonDto;
import com.zubrilka.VideoManager.models.UserRole;
import com.zubrilka.VideoManager.repositories.PersonRepository;
import com.zubrilka.VideoManager.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;


@Service
public class PersonService implements UserDetailsService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;


//        saveTestUsers();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return personRepository
                .findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found: "+username));
    }

    public PersonDto login(String username, String password) throws UnauthorizedException {
        Person person = personRepository.findByUsername(username).orElse(null);

        if (person!=null&&passwordEncoder.matches(password, person.getPassword())){
            String accessToken = jwtUtil.generateAccessToken(person);
            String refreshToken = jwtUtil.generateRefreshToken(person);
            return convertPersonToPersonDto(person,accessToken,refreshToken);
        }else{
            throw new UnauthorizedException("Invalid username or password.");
        }
    }

    private PersonDto convertPersonToPersonDto(Person person, String accessToken, String refreshToken) {
        return new PersonDto(
                person.getUuid(),
                person.getUsername(),
                person.getRole(),
                person.getCreated_at(),
                accessToken,
                refreshToken
                );
    }
    private void saveTestUsers(){
        Person admin = new Person(null,
                passwordEncoder.encode("password"),
                "admin",
                UserRole.ROLE_ADMIN,
                new Timestamp(System.currentTimeMillis()),
                null);
        personRepository.save(admin);
    }
}
