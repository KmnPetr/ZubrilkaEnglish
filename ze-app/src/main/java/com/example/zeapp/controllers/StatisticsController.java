package com.example.zeapp.controllers;

import com.example.zeapp.models.StatisticsDTO;
import com.example.zeapp.services.StatisticsServise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.Duration;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/stat")
public class StatisticsController {
    private final StatisticsServise statisticsServise;
    @Autowired
    public StatisticsController(StatisticsServise statisticsServise) {
        this.statisticsServise = statisticsServise;
    }

    /**
     * Выдаст первых 1500 юзеров с наибольшим количеством очков
     * включая один обьект статистики пользователя от которого пришел запрос, его id будет в параметрах пути
     */
    @GetMapping("/first1500users_rating")
    public Flux<StatisticsDTO> getFirst1500(@RequestParam(name = "ownId") Optional<Long> ownId){
        return statisticsServise.getFirst1500users_rating(ownId.orElse(null))
                .timeout(Duration.ofSeconds(10))
                .retry(3);
    }



    /**
     * сохранит количество очков заработанных в офлайн режимах тренировки
     */
    @PostMapping("/save_offline_points")
    public Mono<String> saveOfflinePoints(@RequestParam int offlinePoints, Mono<Principal> principal) {
        return principal
                .flatMap(principal1 -> statisticsServise
                        .saveOfflinePoints(offlinePoints,principal1.getName()))
                .map(it->"{\"message\": \"Received offlinePoints = " + offlinePoints + "\"}"
                );//чтобы на той стороне json конвертер не ругался отошлем ему такую строку
    }
}
