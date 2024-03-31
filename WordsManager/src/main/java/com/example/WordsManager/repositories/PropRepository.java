package com.example.WordsManager.repositories;

import com.example.WordsManager.models.PropModel;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PropRepository extends ReactiveCrudRepository<PropModel,String> {
    Mono<PropModel> findByKey(String key);
    @Modifying
    @Query("UPDATE properties SET value=:value WHERE key=:key")
    Mono<Integer> setNewValue(@Param("key")String key, @Param("value")String value);
}
