package com.example.WordsManager.repositories;

import com.example.WordsManager.models.VoiceFile;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface VoiceFilesRepository extends ReactiveCrudRepository<VoiceFile, Integer> {
    @Query("SELECT file_name FROM voice_files")
    Flux<String> getAllFileNames();

    @Query("SELECT EXISTS(SELECT 1 FROM voice_files WHERE file_name = :file_name)")
    Mono<Boolean> checkExistenceVoice(@Param("file_name")String file_name);
}
