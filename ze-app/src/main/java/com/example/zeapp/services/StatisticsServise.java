package com.example.zeapp.services;

import com.example.zeapp.models.Statistics;
import com.example.zeapp.repositories.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * сервис занимается обработкой данных по статистике пользователей
 */
@Service
public class StatisticsServise {

    private final TransactionalOperator transactionalOperator;
    private final StatisticsRepository statisticsRepository;
    @Autowired
    public StatisticsServise(TransactionalOperator transactionalOperator, StatisticsRepository statisticsRepository) {
        this.transactionalOperator = transactionalOperator;
        this.statisticsRepository = statisticsRepository;
    }

    @Transactional
    public Mono<Statistics> updatePoints(Long personId,Long earnedPoints) {
        return statisticsRepository.findByPersonId(personId)
                .flatMap(statistics -> {
                    // Запись существует, обновляем points
                    return statisticsRepository.save(addPointsAndRefreshData(statistics,earnedPoints));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // Запись не существует, создаем новую запись
                    Statistics newStatistics = new Statistics();
                    newStatistics.setPersonId(personId);
                    return statisticsRepository.save(addPointsAndRefreshData(newStatistics,earnedPoints));
                }));
    }

    /**
     * прибавит или убавит очки
     * обновит дату
     */
    private Statistics addPointsAndRefreshData(Statistics statistics, Long earnedPoints){
        statistics.setPoints(statistics.getPoints() + earnedPoints);
        if (statistics.getPoints()<0) statistics.setPoints(0L);
        statistics.setNewPoints(statistics.getNewPoints() +earnedPoints);
        statistics.setLastEntry(LocalDateTime.now());

        return statistics;
    }
}
