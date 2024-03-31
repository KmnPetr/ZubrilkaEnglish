package com.example.WordsManager.services;

import com.example.WordsManager.models.VoiceFile;
import com.example.WordsManager.repositories.VoiceFilesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class VoiceFileService {
    private final VoiceFilesRepository voiceFilesRepository;
    private final String pathFolder = "src/main/resources/static/voice/";

    @Autowired
    public VoiceFileService(VoiceFilesRepository voiceFilesRepository) {
        this.voiceFilesRepository = voiceFilesRepository;
    }


    /**
     * сохранит новые Voice в базу данных
     */
    public void saveNewVoicesToDB(){
        //мапа будет содержать имена файлов уже существующих в БД
        //по ней буду сверяться, чтобы не отправлять в БД Voice с уже существующими именами, чтобы не вызывать ошибку
        Map<String, Objects> namesMap = new HashMap<>();
        //запросим список имен файлов имеющихся в БД и сложим из в мапу
        voiceFilesRepository
                .getAllFileNames()
                .collectList()
                .block()
                .forEach(name->namesMap.put(name,null));


        List<String> listLocalNames = getListFiles(pathFolder);//список файлов Voice находящихся в локальной папке
        log.info("Общее количество файлов voice: {}",listLocalNames.size());
        List<String> finalListNames = listLocalNames
                .stream()
                .filter(it-> !namesMap.containsKey(it))
                .toList();//получили список имен файлов не содержащихся в БД
        log.info("Количество новых несохраненных файлов: {}",finalListNames.size());

        List<VoiceFile> voiceFiles = collectListEntity(finalListNames);//получили список обьектов VoiceFile с заполненными полями fileName и fileData
        voiceFilesRepository
                .saveAll(voiceFiles)
                .blockLast();//сохранили все новые voice в БД
    }

    /**
     * метод достанет из папки static/voice файлы и сохранит их в БД
     */
    @Async
    public void printListFilesAndSaveDB(){
        List<String> listFiles = getListFiles(pathFolder);
        List<VoiceFile> voiceFiles = collectListEntity(listFiles);
        voiceFiles.stream().forEach(it->{System.out.println("Файл: "+it.getFileName()+ " \t\tРазмер: "+it.getFileData().length);});

        voiceFilesRepository
                .saveAll(voiceFiles)
                .subscribe(
                        // Этот блок кода будет выполнен при успешном сохранении одного элемента
                        result -> System.out.println("File \""+result.getFileName()+"\" was been saved"),
                        error -> System.err.println("Error during save: " + error),
                        // Этот блок кода будет выполнен после завершения операции (в том числе успешной и с ошибкой)
                        () -> System.out.println("Save operation completed")
                );
    }

    /**
     * функция вернет список обьектов VoiceFile с заполненными полями fileName и fileData
     */
    private List<VoiceFile> collectListEntity(List<String> filenames){
        return filenames
                .stream()
                .map(it-> VoiceFile
                        .builder()
                        .fileName(it)
                        .fileData(getByteFile(pathFolder+it)).build())
                .toList();
    }

    /**
     * метод вернет массив байтов файла по переданному ему пути к файлу
     */
    public byte[] getByteFile(String filePath) {
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] byteArray = new byte[(int) file.length()];
            fis.read(byteArray);
            return byteArray;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * метод выдаст список файлов из папки по указанному пути
     */
    private List<String> getListFiles(String path){

            File resourceFolder = null;
            try {
                resourceFolder = new File(path);
            }catch (Exception e){e.printStackTrace();}

            File[] files = null;
            if (resourceFolder.isDirectory()) {
                files = resourceFolder.listFiles();
            } else {System.out.println("Папка " + path + " не найдена.");}

        List<String> listFileNames = null;
            if (files != null) {
                listFileNames = Arrays.stream(files).map(File::getName).toList();
            }


            return listFileNames;
    }

    /**
     * сохранит файл в локальной папке проекта
     */
    public boolean saveVoiceInLocalFolder(VoiceFile voiceFile) {


        for (String filename : getListFiles(pathFolder)) {
            if (filename.equals(voiceFile.getFileName())){
                //выкенем исключение если voice с таким именем уже был добавлен ранее
                throw new IllegalArgumentException("Файл с именем \""+filename+"\" уже существует");
            }
        }

        File file = new File(pathFolder+voiceFile.getFileName());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(voiceFile.getFileData());
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * проверит: существует ли voice с таким именем в БД
     */
    public Mono<Boolean> checkExistenceVoiceInDB(String voiceName) {
        return voiceFilesRepository.checkExistenceVoice(voiceName);
    }

    public boolean checkExistenceVoiceInFiles(String voiceName) {
        List<String> listFiles = getListFiles(pathFolder);
        for (String fileName : listFiles) {
            if (voiceName.equals(fileName)) return true;
        }
        return false;
    }
}
