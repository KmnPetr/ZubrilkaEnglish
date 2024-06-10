package com.example.zeapp.controllers;

import com.example.zeapp.models.ProfileDTO;
import com.example.zeapp.models.PropModel;
import com.example.zeapp.services.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/profile")
@Slf4j
public class ProfileController {
    private final PersonService personService;
    @Autowired
    public ProfileController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * метод обновляет информацию по некоторым полям пользователя например email, name или другое
     * он принимает PropModel  json
     * в поле key - название поля для замены
     * в поле value - новое желаемое значение
     */
    @PatchMapping("/update-field/{id}")
    public Mono<ProfileDTO> updateProfileField(@PathVariable long id, @RequestBody Mono<PropModel> fieldInfo) {
        return fieldInfo.flatMap(propModel->personService.changeFieldOfUsersProfile(id,propModel.getKey(),propModel.getValue()));
    }
}
