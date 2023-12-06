package com.example.ZubrilkaEnglishServer.controllers;

import com.example.ZubrilkaEnglishServer.controllers.exeptions.MyExeption;
import com.example.ZubrilkaEnglishServer.models.PropModel;
import com.example.ZubrilkaEnglishServer.services.PropServise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/properties")
public class PropController {
    private final PropServise propServise;
    @Autowired
    public PropController(PropServise propServise) {
        this.propServise = propServise;
    }

    /**
     * метод выдаст дату последнего обновления таблицы Word БД
     */
    @GetMapping("/get_update_at")
    public PropModel getUpdateAt()throws MyExeption {
        return propServise.getUpdatedAt();
    }

    /**
     * метод установит новое значение update_at в таблице properties
     */
    @GetMapping("/set_update_at")//ограничено, только для админа
    public String setUpdateAt(){
        propServise.setUpdateAt();
        return "The DB update time is successfully set";
    }
}
