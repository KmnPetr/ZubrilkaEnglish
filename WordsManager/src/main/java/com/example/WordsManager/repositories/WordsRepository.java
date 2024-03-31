package com.example.WordsManager.repositories;

import com.example.WordsManager.models.Word;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface WordsRepository extends ReactiveCrudRepository<Word,Integer> {

    @Modifying
    @Query("UPDATE Word SET sorting_value=:sorting_value WHERE id=:id")
    Mono<Integer> updateSortingValue(@Param("sorting_value")Integer sorting_value, @Param("id")Integer id);

}
