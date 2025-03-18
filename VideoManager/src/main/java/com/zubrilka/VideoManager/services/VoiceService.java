package com.zubrilka.VideoManager.services;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.dto.VoiceDto;
import com.zubrilka.VideoManager.enums.Sex;
import com.zubrilka.VideoManager.models.*;
import com.zubrilka.VideoManager.repositories.VoiceRepository;
import com.zubrilka.VideoManager.util.MediaLocalStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class VoiceService {
    private final VoiceRepository voiceRepository;
    private final MediaLocalStorage mediaLocalStorage;
    private final TranslationService translationService;



    @Autowired
    public VoiceService(VoiceRepository voiceRepository, MediaLocalStorage mediaLocalStorage, TranslationService translationService) {
        this.voiceRepository = voiceRepository;
        this.mediaLocalStorage = mediaLocalStorage;
        this.translationService = translationService;
    }

    /**
     * принимает wav файл конвертирует в mp3 сохраняет в локальном хранилище
     */
    @Transactional
    public UUID saveWavVoice(MultipartFile file, String text, String voice, String sex){

        UUID uuid = UUID.randomUUID();

        String localStoragePath = mediaLocalStorage.saveWavVoice(file,uuid.toString());

        Voice newVoice = new Voice(
                uuid,
                text,
                voice,
                Sex.fromValue(sex),
                localStoragePath
        );
        return voiceRepository.save(newVoice).getUuid();
    }

    /**
     * достанет voice из локального хранилища
     */
    public BufferedInputStream getVoiceMp3(UUID uuid) throws NotFoundException {
        String localLink = voiceRepository.findById(uuid).orElseThrow(() -> new NotFoundException("Voice with uuid %s not found".formatted(uuid))).getLocal_link();
        try{
            return mediaLocalStorage.getVoiceAsMp3(localLink);
        } catch (FileNotFoundException e) {
            throw new NotFoundException("Voice with uuid %s not found".formatted(uuid));
        }
    }

    /**
     * запрос на список voice ранее озвученных схожих по тексту
     * из БД вытягиваются все Voice схожие по полю text
     * далее на основе текущего Translation которым сейчас занимается переводчик, вычисляются наиболее частоупотребимые актеры озвучки
     * на основе их Voice с точным совпадением поля text сортируются
     */
    public List<VoiceDto> findSimilarVoices(String text, UUID translation_uuid) throws NotFoundException {

        List<Voice> similarVoices = voiceRepository.findSimilarVoices(text, 20);
        Translation translation = translationService.getTranslationByUuid(translation_uuid);

        List<Str> flatStrs = new ArrayList<>(); //поскольку Phrase это сложный обьект, все Str обьекты их него переведем в плоский список

        if (translation.getPhrases()!=null){
            for (Phrase phrase : translation.getPhrases()){
                Str cn = phrase.getCn();
                Str ru = phrase.getRu();
                Str en = phrase.getEn();
                if (cn!=null) flatStrs.add(cn);
                if (ru!=null) flatStrs.add(ru);
                if (en!=null) flatStrs.add(en);
                //TODO при добавлении новых языков добавить новый str

                if (phrase.getWords()!=null){
                    for (Word word : phrase.getWords()){
                        Str cn_w = word.getCn();
                        Str ru_w = word.getRu();
                        Str en_w = word.getEn();
                        if (cn_w!=null) flatStrs.add(cn_w);
                        if (ru_w!=null) flatStrs.add(ru_w);
                        if (en_w!=null) flatStrs.add(en_w);
                        //TODO при добавлении новых языков добавить новый str
                    }
                }
            }
        }

        //соберем все использованные в данном переводе voice_uuid
        List<UUID> used_voice_uuid = flatStrs.stream()
                .filter(str->str.getVoice_uuid()!=null)
                .map(Str::getVoice_uuid)
                .toList();


        //запросим в БД использованные в переводе voice чтобы взять оттуда актеров озвучки
        List<Voice> used_voices = voiceRepository.findVoicesByUuidsIn(used_voice_uuid);


        // Сгруппируем голоса актеров и посчитаем количество их использований в переводе
        Map<String, Integer> mapUsedVoices = new HashMap<>();

        used_voices.stream().forEach(voice -> {
            String voice_actor = voice.getVoice(); // Получаем актера озвучки
            // Если актер уже есть в мапе, увеличиваем количество его использований
            mapUsedVoices.put(voice_actor, mapUsedVoices.getOrDefault(voice_actor, 0) + 1);
        });


        List<VoiceDto> dtoList = similarVoices.stream().map(voice->{
            Integer priority = mapUsedVoices.getOrDefault(voice.getVoice(),0);
            return new VoiceDto(
                    voice.getUuid(),
                    voice.getText(),
                    voice.getVoice(),
                    voice.getSex().getValue(),
                    priority
            );
        }).toList();

        List<VoiceDto> exactMatches = new ArrayList<>();
        List<VoiceDto> otherVoices = new ArrayList<>();
        dtoList.stream().forEach(voiceDto -> {
            if (text.equals(voiceDto.getText())) exactMatches.add(voiceDto);
            else otherVoices.add(voiceDto);
        });

        // Сортируем exactMatches по полю priority по убыванию
        exactMatches.sort(Comparator.comparingInt(VoiceDto::getPriority).reversed());

        // Склеиваем exactMatches с otherVoices
        exactMatches.addAll(otherVoices);

        return exactMatches;
    }
}
