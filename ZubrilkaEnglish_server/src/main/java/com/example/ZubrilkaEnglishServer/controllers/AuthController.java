package com.example.ZubrilkaEnglishServer.controllers;

import com.example.ZubrilkaEnglishServer.controllers.exeptions.MyExeption;
import com.example.ZubrilkaEnglishServer.controllers.exeptions.MyValidationExeption;
import com.example.ZubrilkaEnglishServer.dto.PersonDTO;
import com.example.ZubrilkaEnglishServer.models.AuthenticationDTO;
import com.example.ZubrilkaEnglishServer.models.Person;
import com.example.ZubrilkaEnglishServer.security.JWTUtil;
import com.example.ZubrilkaEnglishServer.services.RegistrationServiсe;
import com.example.ZubrilkaEnglishServer.util.PersonValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PersonValidator personValidator;
    private final RegistrationServiсe registrationServiсe;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    @Autowired
    public AuthController(PersonValidator personValidator, RegistrationServiсe registrationServiсe, JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.personValidator = personValidator;
        this.registrationServiсe = registrationServiсe;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

//    /**
//     * страница аутентификации
//     */
//    @GetMapping("/login")
//    public String loginPage(){
//        return "auth/login";
//    }
//    /**
//     * страница регистрации нового юзера
//     */
//    @GetMapping("/registration")
//    public String registrationPage(@ModelAttribute("person") Person person/*полож.пустого чел.в модель*/){
//        return "auth/registration";
//    }
    /**
     * принимаем данные после регистрации
     */
    @PostMapping("/registration")
    public Map<String,String> performRegistration(@RequestBody @Valid PersonDTO personDTO/*получ.данные с формы*/,
                                      BindingResult bindingResult){

        Person person=convertToPerson(personDTO);

        personValidator.validate(person,bindingResult);
        if (bindingResult.hasErrors()){
            Map<String,String> validationErrors=new HashMap<>();
            List<FieldError> errors=bindingResult.getFieldErrors();
            for(FieldError error:errors){
                validationErrors.put(error.getField(), error.getDefaultMessage());
            }
            throw new MyValidationExeption("Validation Errors",validationErrors,HttpStatus.BAD_REQUEST);
        }

        registrationServiсe.register(person);

        String token=jwtUtil.generateToken(person.getEmail());

        return Map.of("jwt-token",token);//TODO тоже заменить мапу на Response кажется  и сделать обработку ошибок
    }

    /**
     * аутентификация
     */
    @PostMapping("/login")
    public Map<String,String> performLogin(@RequestBody AuthenticationDTO authenticationDTO){

        //этот класс передаст данные в секьюрити
        UsernamePasswordAuthenticationToken authInputToken=
                new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(),
                        authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        }catch (BadCredentialsException e){
            throw new MyExeption(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        String token=jwtUtil.generateToken(authenticationDTO.getEmail());
        return Map.of("jwt-token", token);//TODO
    }
    ///////////////////////конвертеры//////////////////////////
    private Person convertToPerson(PersonDTO personDTO){
        return modelMapper.map(personDTO, Person.class);
    }

    ///////////////////Обработчики ошибок///////////////////////
}
