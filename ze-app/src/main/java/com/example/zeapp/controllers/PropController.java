package com.example.zeapp.controllers;

import com.example.zeapp.models.PropModel;
import com.example.zeapp.services.PropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/properties")
public class PropController {
    private final PropService propService;
    @Autowired
    public PropController(PropService propService) {
        this.propService = propService;
    }

    /**
     * метод выдаст дату последнего обновления таблицы Word БД
     */
    @GetMapping("/get_dictionary_version")
    public Mono<PropModel> getDictionaryVersion() {
        return propService.getDictionaryVersion();
    }
}
