package com.example.zeapp.services;

import com.example.zeapp.models.PropModel;
import com.example.zeapp.repositories.PropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
public class PropService {
    private final PropRepository propRepository;
    @Autowired
    public PropService(PropRepository propRepository) {
        this.propRepository = propRepository;
    }


    /**
     * метод выдаст версию последнего обновления словаря Word БД
     */
    public Mono<PropModel> getDictionaryVersion() {
        return propRepository.findByKey("dictionary_version");
    }
}
