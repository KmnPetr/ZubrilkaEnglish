package com.example.zeapp.repositories;

import com.example.zeapp.models.Statistics;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface StatisticsRepository extends ReactiveCrudRepository<Statistics, Long> {
    Mono<Statistics> findByPersonId(Long personId);
}
