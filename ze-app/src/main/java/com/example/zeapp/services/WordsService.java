package com.example.zeapp.services;

import com.example.zeapp.models.Word;
import com.example.zeapp.repositories.WordsRepository;
import com.example.zeapp.services.cache.WordsCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@EnableScheduling
public class WordsService {
    private final WordsRepository wordsRepository;
    private final WordsCache wordsCache;
    private final PropService propService;
    private final VoiceFileService voiceFileService;
    @Autowired
    public WordsService(WordsRepository wordsRepository, WordsCache wordsCache, PropService propService, VoiceFileService voiceFileService) {
        this.wordsRepository = wordsRepository;
        this.wordsCache = wordsCache;
        this.propService = propService;
        this.voiceFileService = voiceFileService;
    }

    /**
     * выдаст список слов для первоначальной установки их в качестве учебных
     * вызывается фронтом при первоначальной установке приложения
     */
    public Flux<Word> getInitialTrainingList() {
        //список id получен при помощи вызова sql команды SELECT string_agg(id::text, ',') AS ids FROM word WHERE foreign_word = ANY (ARRAY['car','drive','door','flower','book','write','read','guitar','house','ice','juice','jump','orange','pencil','river','swim','tree','window','sing']);
        return wordsRepository.findAllById(List.of(80,94,109,148,235,329,641,644,713,718,1088,1210,1239,1243,1803,1882,1883,1888,1980,2591));
    }

    /**
     * метод возвращает флакс на запрос списка всех Words из DB
     */
    public Flux<Word> getAllWords(){
        return Flux
                .fromIterable(wordsCache.getListAllWords());
    }
    
    @Scheduled(fixedDelay = 2000)
    private void upgradeCache(){
        Integer DBdicVers = Integer.valueOf(Objects.requireNonNull(propService.getDictionaryVersion().block()).getValue());
        if (DBdicVers>wordsCache.getDictionaryVersion()){
            log.info("Начата замена кэша WordsCache.");
            //уведомим voiceFileService о необходимости замены кэша
            voiceFileService.replaceCache(DBdicVers);

            List<Word> newList = wordsRepository.findAll().toStream().toList();
            wordsCache.setListAllWords(newList);

            wordsCache.setDictionaryVersion(DBdicVers);

            log.info("Произведена замена кэша WordsCache.");

        }
    }

}