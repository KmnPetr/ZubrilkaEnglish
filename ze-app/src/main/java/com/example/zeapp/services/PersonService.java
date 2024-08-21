package com.example.zeapp.services;

import com.example.zeapp.controllers.validation.UnauthorizedException;
import com.example.zeapp.controllers.validation.ValidationException;
import com.example.zeapp.models.Person;
import com.example.zeapp.models.ProfileDTO;
import com.example.zeapp.models.UserRole;
import com.example.zeapp.repositories.PersonRepository;
import com.example.zeapp.security.JwtUtil;
import jakarta.validation.constraints.AssertTrue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.regex.Pattern;

@Service
@Slf4j
public class PersonService implements ReactiveUserDetailsService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * метод попытается сгенерировать нового временного пользователя а также
     * в случае уже существующего пользователя повторит генерацию
     */
    public Mono<ProfileDTO> getTemporaryProfile() {
        return generateAndRegisterProfile()
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(10))
                        .filter(throwable -> throwable instanceof ValidationException
                                && throwable.getMessage().equals("This email is already in use.")));
    }
    /**
     * выдаст временный профиль со случайным именем
     */
    public Mono<ProfileDTO> generateAndRegisterProfile() {
        String name = "Guest_"+generateRandomString(8);
        String email = name+"@zubrilka.en";
        String password = generateRandomString(8);
        Person person = new Person(
                null,
                email,
                password,
                name,
                null,
                null
        );

        return registerPerson(Mono.just(person));
    }

    /**
     * Создаст рандомную строку для имени пользователя
     */
    public String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return personRepository.findByEmail(username).cast(UserDetails.class);
    }

    @AssertTrue
    public Mono<ProfileDTO> registerPerson(Mono<Person> requestedPerson){
        return requestedPerson
                .flatMap(rPerson ->{
                    return personRepository
                            .existsByEmail(rPerson.getEmail())
                            .flatMap(aBoolean -> {
                                if (!aBoolean){
                                    rPerson.setRole(UserRole.ROLE_USER);
                                    rPerson.setCreated_at(new Timestamp(System.currentTimeMillis()));
                                    rPerson.setPassword(passwordEncoder.encode(rPerson.getPassword()));
                                    return personRepository.save(rPerson).flatMap(this::convertToProfileDTO);
//                                            .map(this::convertToProfileDTO);
                                } else {
                                    return Mono.error(new ValidationException("This email is already in use."));
                                }
                            });

                        }
        );
    }

    /**
     * аутентифицирует пользователя,
     * выдаст токены или ошибки
     */
    public Mono<ProfileDTO> login(String username, String password) {

        return findByUsername(username)
                .cast(Person.class)
//        return personRepository
//                .findByEmail(username)
                .flatMap(person ->{
                            if (passwordEncoder.matches(password,person.getPassword())){
                                return convertToProfileDTO(person);
//                                return Mono.just(convertToProfileDTO(person));
                            }else {
                                return Mono.error(new UnauthorizedException("Invalid login or password."));
                            }
                        }
                )
                .switchIfEmpty(Mono.error(new UnauthorizedException("Invalid login or password.")));
    }
    //конвертирует Person в ProfileDTO
    private Mono<ProfileDTO> convertToProfileDTO(Person person){
        return Mono.just(new ProfileDTO(
                (long)person.getId(),
                person.getEmail(),
                null,
                person.getShort_name(),
                jwtUtil.generateAccessToken(person),
                jwtUtil.generateRefreshToken(person),
                person.getCreated_at()
        ));
    }

    /**
     * метод обновляет информацию по некоторым полям пользователя например email, name или другое
     * он принимает PropModel  json
     * в поле fieldName - название поля для замены
     * в поле newValue - новое желаемое значение
     */
    public Mono<ProfileDTO> changeFieldOfUsersProfile(long id, String fieldName, String newValue) {
        if (newValue.isEmpty()) return Mono.error(new ValidationException("Field value is blank."));

        switch (fieldName) {
            case "name" -> {
                return personRepository.updateUserName(id, newValue).flatMap(this::convertToProfileDTO);
//                return personRepository.updateUserName(id, newValue).map(this::convertToProfileDTO);
            }
            case "email" -> {
                if (isEmailValid(newValue)){
                    return personRepository.existsByEmail(newValue)
                            .flatMap(result->{
                                if (!result){
                                    return personRepository.updateUserEmail(id, newValue).flatMap(this::convertToProfileDTO);
//                                    return personRepository.updateUserEmail(id, newValue).map(this::convertToProfileDTO);
                                } else {
                                    return Mono.error(new ValidationException("This Email is already used."));
                                }
                            });
                } else {
                    return Mono.error(new ValidationException("Email is invalid."));
                }

            }
            default -> {
                return Mono.error(new ValidationException("Fields name \"" + fieldName + "\" is invalid."));
            }
        }
    }

    /**
     * проверит строку email на валидность
     * @param email
     * @return
     */
    private boolean isEmailValid(String email){
        return Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matcher(email).matches();
    }

    /**
     * Вернет Mono<Person> из БД по id
     */
    public Mono<Person> findPersonById(Long personId) {
        return personRepository.findById(personId.intValue());
    }

}