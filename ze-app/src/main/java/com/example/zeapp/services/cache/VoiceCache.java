package com.example.zeapp.services.cache;

import com.example.zeapp.models.VoiceFile;
import com.example.zeapp.repositories.VoiceFilesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@Slf4j
public class VoiceCache {
    private String pathFolder;
    private Integer dicVers = 0;

    //переменная replacementProcess(replcProc) указывает, происходит ли на данный момент замена кэша
    private Boolean replcProc = false;
    private final VoiceFilesRepository voiceFilesRepository;
    @Autowired
    public VoiceCache(VoiceFilesRepository voiceFilesRepository,@Value("${voicePathFolder}")String voicePathFolder) {
        this.voiceFilesRepository = voiceFilesRepository;
        this.pathFolder = voicePathFolder;

        //первоначальное создание папки
        createNewFolder(pathFolder+dicVers);
    }


    /**
     * метод выдаст VoiceFile из файлов из архива
     */
    public Mono<VoiceFile> getVoiseFile(String name) {
        VoiceFile voiceFile = new VoiceFile();
        voiceFile.setFileName(name);
        voiceFile.setFileData(getByteFile(pathFolder+dicVers+"/"+name));
        return Mono.just(voiceFile);
    }

    /**
     * метод укажет, происходит ли на данный момент процесс замены кэша
     */
    public Boolean getReplcProc(){
        return replcProc;
    }

    /**
     * метод произведет замену кэша в VoiceCache
     * на вход следует передать новое значение dictionaryVersion из БД
     */
    @Async
    public void replaceCache(Integer dBdicVers) {
        replcProc = true;
        log.info("начата замена кэша VoiceCache с версии {} до версии {}",dicVers,dBdicVers);
        Long startTime = System.currentTimeMillis();

        String oldPath = pathFolder+dicVers+"/";
        String newPath = pathFolder+dBdicVers+"/";

        createNewFolder(newPath);


        voiceFilesRepository
                .findAll()
                .buffer(100)
                .subscribe(
                        result->{
                            result.forEach(it->{
                                createNewFile(it,newPath);
//                                log.info("Создан файл: {}",it.getFileName());
                            });
                        },
                        error->{},
                        ()->{
                            dicVers = dBdicVers;
                            replcProc = false;
                            Double duration = (double)(System.currentTimeMillis()-startTime)/1000;
                            log.info("Окончена замена кэша VoiceCache. Новая версия: {}. Продолжительность: {} секунд.",dicVers,duration);

                            clearDirectory(new File(oldPath));
                        }
                );
    }

    /**
     * метод создаст новый аудио файл
     */
    public void createNewFile(VoiceFile voice, String newPath){
        try{
            FileOutputStream fos = new FileOutputStream(newPath+voice.getFileName());
            fos.write(voice.getFileData());
            fos.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * метод создаст новую папку
     */
    private void createNewFolder(String newPath){
        try{
            Files.createDirectories(Path.of(newPath));
        }catch (IOException e){e.printStackTrace();}
    }


    /**
     * метод очистит папку по указанному пути
     */
    private void clearDirectory(File directory) {
        if (directory.exists() && directory.isDirectory()) {
            // Получаем все файлы и подпапки внутри текущей папки
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // Рекурсивно вызываем clearDirectory для удаления содержимого подпапки
                        clearDirectory(file);
                        file.delete();
                    } else {
                        // Удаляем файл
                        file.delete();
                    }
                }
            }
        }
    }

    /**
     * метод выдаст текущую версию словаря, по которой он обновляет свои VoiceFile
     */
    public Integer getDictionaryVersion() {
        return dicVers;
    }

    /**
     * метод вернет массив байтов файла по переданному ему пути к файлу
     */
    public byte[] getByteFile(String filePath){
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] byteArray = new byte[(int) file.length()];
            fis.read(byteArray);
            return byteArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
