package com.zubrilka.VideoManager.services;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.controllers.validation.UnauthorizedException;
import com.zubrilka.VideoManager.models.Person;
import com.zubrilka.VideoManager.dto.PersonDto;
import com.zubrilka.VideoManager.repositories.PersonRepository;
import com.zubrilka.VideoManager.security.JwtUtil;
import com.zubrilka.VideoManager.security.jwtWeb.Token;
import com.zubrilka.VideoManager.security.jwtWeb.TokenUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service
@Slf4j
@Transactional(readOnly = true)
public class PersonService implements UserDetailsService, AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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
                person.getShort_name(),
                person.getSex(),
                person.getRole(),
                person.getCreated_at(),
                accessToken,
                refreshToken
                );
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken)
            throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof Token token) {
            return new TokenUser(
                    token.getSubject(),
                    "nopassword",
                    true,
                    true,
                    true,
//                    !this.jdbcTemplate.queryForObject("""
//                            select exists(select id from t_deactivated_token where id = ?)
//                            """, Boolean.class, token.id()) &&
//                            token.expiresAt().isAfter(Instant.now()),//проверка на истечение срока действия токена, при корректной работе логаута информация о заблокированном токене содержится в БД
                    true,
                    token.getAuthorities().stream().map(SimpleGrantedAuthority::new).toList(),
                    token);
        }

        throw new UsernameNotFoundException("Principal must me of type Token");
    }

    public Person getPersonByName_v2(String username){
       return personRepository.findByUsername(username).orElse(null);
    }

    /**
     * it will find a person by name in the database
     */
    public PersonDto getPersonByName(String username) {
        Person person = personRepository.findByUsername(username).orElse(null);

        String accessToken = jwtUtil.generateAccessToken(person);
        String refreshToken = jwtUtil.generateRefreshToken(person);
        return convertPersonToPersonDto(person,accessToken,refreshToken);
    }

    /**
     * обновит пользовательские настройки rating_voices
     */
    @Transactional
    public void updateRatingVoices(Map<String, Integer> ratingVoices, String username) throws NotFoundException {
        Person person = personRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found."));
        person.setRating_voices(ratingVoices);
        personRepository.save(person);
    }

    /**
     * выдаст пользовательские настройки rating_voices
     */
    public Map<String, Integer> getRatingVoices(String username) throws NotFoundException {
        return personRepository.findByUsername(username).orElseThrow(()->new NotFoundException("User not found.")).getRating_voices();
    }
}

