package com.example.WordsManager.services;

import com.example.WordsManager.models.Word;
import com.example.WordsManager.repositories.WordsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@EnableScheduling
public class WordsService {
    private final WordsRepository wordsRepository;
    private final WordsCache wordsCache;
    private final PropService propService;
    private final SerializedWordStore serializedWordStore;
    private final VoiceFileService voiceFileService;
    @Autowired
    public WordsService(WordsRepository wordsRepository, WordsCache wordsCache, PropService propService, SerializedWordStore serializedWordStore, VoiceFileService voiceFileService) {
        this.wordsRepository = wordsRepository;
        this.wordsCache = wordsCache;
        this.propService = propService;
        this.serializedWordStore = serializedWordStore;
        this.voiceFileService = voiceFileService;
    }

    /**
     * сохранит или обновит Word в БД из локального сериализованного хранилища
     */
    public void saveOrUpdateWords() {
        while (true){
            Word nextWord = serializedWordStore.getFirtWord();
            if (nextWord!=null){//words в хранилище еще не кончились, продолжаем..

                //проверим имеется ли voice для данного word в БД
                Boolean voiceIsExistInDB = voiceFileService.checkExistenceVoiceInDB(nextWord.getLink_voice()).block();
                if (!voiceIsExistInDB) throw new RuntimeException("The \""+nextWord.getLink_voice()+"\" file required for the Word \""+nextWord.getForeignWord()+"\" is missing from the Database");
                boolean voiceIsExistInFiles = voiceFileService.checkExistenceVoiceInFiles(nextWord.getLink_voice());
                if (!voiceIsExistInFiles) throw new RuntimeException("The \""+nextWord.getLink_voice()+"\" file required for the Word \""+nextWord.getForeignWord()+"\" is missing from the local files");
                //проверим имеется ли voice для данного word в Локальной папке
                Word savedWord = wordsRepository.save(nextWord).block();
                if (savedWord!=null){
                    log.info("Сохранение/обновление слова \"{}\" прошло успешно.",savedWord.getForeignWord());
                    serializedWordStore.deleteFirstWord(savedWord);
                }
            }else break;
        }
    }

    /**
     * метод возвращает флакс на запрос списка всех Words из DB
     */
    public Flux<Word> getAllWords(){
        return Flux
                .fromIterable(wordsCache.getListAllWords());
    }

    /**
     * метод возвращает список моно на запрос одного экземпляра Word из DB по id
     */
    public Mono<Word> getWordById(Integer idWord) {
        return wordsRepository.findById(idWord);
    }
    
    @Scheduled(fixedDelay = 2000)
    private void upgradeCache(){
        Integer DBdicVers = Integer.valueOf(Objects.requireNonNull(propService.getDictionaryVersion().block()).getValue());

        if (DBdicVers>wordsCache.getDictionaryVersion()){
            List<Word> newList = wordsRepository.findAll().toStream().toList();
            wordsCache.setListAllWords(newList);

            wordsCache.setDictionaryVersion(DBdicVers);

            log.info("Произведена замена кэша.");
        }
    }

    /**
     * обновит значения sorting_value всех переданных списка слов
     */
    public void updateSortingValues(List<Word> wordList) {
        int i = 0;
        for (Word w : wordList) {
            wordsRepository.updateSortingValue(w.getSorting_value(),w.getId()).block();

            if ((i%300)==0){
                //выполнение метода долгое, поэтому сделаем некий вывод в консоль
                int percent = (int) ((double) i/ wordList.size()*100);
                System.out.println("Отправка в БД.. "+percent+"%");
            }
            i++;
        }
    }

}