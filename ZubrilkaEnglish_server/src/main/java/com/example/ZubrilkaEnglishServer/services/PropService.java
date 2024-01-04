package com.example.ZubrilkaEnglishServer.services;


import com.example.ZubrilkaEnglishServer.controllers.exeptions.MyExeption;
import com.example.ZubrilkaEnglishServer.models.PropModel;
import com.example.ZubrilkaEnglishServer.repositories.PropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PropService {
    private final PropRepository propRepository;
    @Autowired
    public PropService(PropRepository propRepository) {
        this.propRepository = propRepository;
    }
    /**
     * метод выдаст дату последнего обновления таблицы Word БД
     */
    public PropModel getDictionaryVersion(){
        PropModel propModel=propRepository.findByKey("dictionary_version").orElse(null);
        if(propModel!=null&&propModel.getValue()!=null){
            return propModel;
        }else{
            System.out.println("propModel==null");
            throw new MyExeption("The time of the last database update is not set.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
