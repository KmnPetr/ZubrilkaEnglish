package com.zubrilka.VideoManager.controllers;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/person")
public class PersonController {
    private final PersonService personService;
    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    //обновит рейтинг голосов
    //для простоты помещен в один обьект Person
    //так как он привязан к конкретному переводчику
    @PostMapping("/rating_voices")
    public void updateRatingVoices(
            @RequestBody Map<String, Integer> ratingVoices,
            @AuthenticationPrincipal UserDetails userDetails) throws NotFoundException {

        personService.updateRatingVoices(ratingVoices,userDetails.getUsername());
    }

    //выдаст рейтинг голосов
    //для простоты помещен в один обьект Person
    //так как он привязан к конкретному переводчику
    @GetMapping("/rating_get")
    public Map<String, Integer> getRatingVoices(@AuthenticationPrincipal UserDetails userDetails) throws NotFoundException {
        return personService.getRatingVoices(userDetails.getUsername());
    }
}
