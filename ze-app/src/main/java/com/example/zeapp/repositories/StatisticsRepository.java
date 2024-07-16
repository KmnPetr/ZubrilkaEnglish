package com.example.zeapp.repositories;

import com.example.zeapp.models.Statistics;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface StatisticsRepository extends ReactiveCrudRepository<Statistics, Long> {
    Mono<Statistics> findByPersonId(Long personId);

    @Query("SELECT * FROM Statistics WHERE person_id = (SELECT id FROM Person WHERE email = :userName)")
    Mono<Statistics> findByPersonUsername(String userName);
}