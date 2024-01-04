package com.example.ZubrilkaEnglishServer.controllers;

import com.example.ZubrilkaEnglishServer.controllers.exeptions.MyExeption;
import com.example.ZubrilkaEnglishServer.models.PropModel;
import com.example.ZubrilkaEnglishServer.services.PropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public PropModel getDictionaryVersion()throws MyExeption {
        return propService.getDictionaryVersion();
    }
}
