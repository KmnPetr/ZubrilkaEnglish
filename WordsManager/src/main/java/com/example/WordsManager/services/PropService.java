package com.example.WordsManager.services;

import com.example.WordsManager.models.PropModel;
import com.example.WordsManager.repositories.PropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Transactional(readOnly = true)
public class PropService {
    private final PropRepository propRepository;
    @Autowired
    public PropService(PropRepository propRepository) {
        this.propRepository = propRepository;
    }


    /**
     * метод установит новое значение dictionary_version в таблице properties
     */
    @Transactional
    public void setDictionaryVersion() {
//        propRepository.setNewValue("dictionary_version", ZonedDateTime.now().toString()); //TODO теперь используем int вместо формата времени
    }

    /**
     * метод выдаст версию последнего обновления словаря Word БД
     */
    public Mono<PropModel> getDictionaryVersion() {
        return propRepository.findByKey("dictionary_version");
    }

    /**
     * увеличит версию словаря на 1
     */
//    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Transactional
    public Mono<Integer> increaseDictionaryVersion() {
//        propRepository.findByKey("dictionary_version").subscribe(
//                next -> {
//                    System.out.println("Обработка корректного ответа");
//                    int version = Integer.parseInt(next.getValue());
//                    version++;
//                    Mono<Integer> dictionaryVersion = propRepository.setNewValue("dictionary_version", String.valueOf(version));
//                    dictionaryVersion.subscribe(System.out::println);
//
//                },
//                error -> {
//                    System.out.println("Обработка ошибки");
//                    error.printStackTrace();
//                    },
//                () -> {
//                    System.out.println("Обработка завершения потока");
//                }
//
//        );

        return propRepository.findByKey("dictionary_version")
                .publishOn(Schedulers.boundedElastic())
                .mapNotNull(it->{
                    System.out.println("Обработка корректного ответа");
                    int version = Integer.parseInt(it.getValue());
                    version++;
                    Mono<Integer> dictionaryVersion = propRepository.setNewValue("dictionary_version", String.valueOf(version));
                    return dictionaryVersion.block();
                });
    }
}
