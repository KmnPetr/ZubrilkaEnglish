package com.example.ZubrilkaEnglishServer.util;

import com.example.ZubrilkaEnglishServer.models.Person;
import com.example.ZubrilkaEnglishServer.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * класс проверяет на уникальность регистрации имени нового человека
 */
@Component
public class PersonValidator implements Validator {
    private final PersonService personService;
    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    /**
     * нужен для определения того класса который хотим валидировать
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    /**
     * метод делает запрос в БД, существует ли Person c таким email
     * если да, то кладет ошибку
     */
    @Override
    public void validate(Object target, Errors errors) {
        Person person=(Person)target;
        if(personService.findOneByEmail(person.getEmail())!=null)
            errors.rejectValue("email"/*на каком поле*/,
                "",
                "This email is already taken"/*сообщение*/);
    }
}
