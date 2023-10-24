package com.example.ZubrilkaEnglishServer.controllers;

import com.example.ZubrilkaEnglishServer.models.Person;
import com.example.ZubrilkaEnglishServer.security.PersonDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    @GetMapping("/hello")
    public String sayHello(){
        return "hello";
    }

    /**
     * метод получения данных пользователя
     * был как тренировочный вариант но может пригодится
     */
    @GetMapping("/showUserInfo")
    @ResponseBody
    public ResponseEntity<?> showUserInfo(){

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails=(PersonDetails) authentication.getPrincipal();

        Person person=personDetails.getPerson();
        person.setPassword(null);

        return new ResponseEntity<>(person, HttpStatus.OK);
    }
    @GetMapping("/admin")
    public String adminPage(){
        return "admin";
    }

}
