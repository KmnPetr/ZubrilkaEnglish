package com.example.zeapp.repositories;

import com.example.zeapp.models.VoiceFile;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface VoiceFilesRepository extends ReactiveCrudRepository<VoiceFile, Integer> {

    @Query("SELECT * FROM voice_files WHERE file_name=:fileName")
    Mono<VoiceFile> findByFileName(String fileName);

    /**
     * выдаст список всех хранимых имен файлов
     */
    @Query("SELECT file_name FROM voice_files")
    Flux<String> getAllNames();
}