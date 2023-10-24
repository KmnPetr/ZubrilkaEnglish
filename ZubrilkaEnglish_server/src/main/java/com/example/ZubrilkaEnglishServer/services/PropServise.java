package com.example.ZubrilkaEnglishServer.services;


import com.example.ZubrilkaEnglishServer.controllers.exeptions.MyExeption;
import com.example.ZubrilkaEnglishServer.models.PropModel;
import com.example.ZubrilkaEnglishServer.repositories.PropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Service
@Transactional(readOnly = true)
public class PropServise {
    private final PropRepository propRepository;
    @Autowired
    public PropServise(PropRepository propRepository) {
        this.propRepository = propRepository;
    }
    /**
     * метод выдаст дату последнего обновления таблицы Word БД
     */
    public PropModel getUpdatedAt(){
        PropModel propModel=propRepository.findByKey("update_at").orElse(null);
        if(propModel!=null&&propModel.getValue()!=null){
            return propModel;
        }else{
            System.out.println("propModel==null");
            throw new MyExeption("The time of the last database update is not set.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * метод установит новое значение update_at в таблице properties
     */
    @Transactional
    public void setUpdateAt() {
        propRepository.setNewValue("update_at", ZonedDateTime.now().toString());
    }
}
