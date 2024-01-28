package com.example.zeapp.repositories;

import com.example.zeapp.models.VoiceFile;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface VoiceFilesRepository extends ReactiveCrudRepository<VoiceFile, Integer> {

    @Query("SELECT * FROM voice_file WHERE file_name=:fileName")
    Mono<VoiceFile> findByFileName(@Param("fileName") String fileName);
}