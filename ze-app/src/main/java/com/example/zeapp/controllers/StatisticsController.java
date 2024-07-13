package com.example.zeapp.controllers;

import com.example.zeapp.models.StatisticsDTO;
import com.example.zeapp.services.StatisticsServise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

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
        return statisticsServise.getFirst1500users_rating(ownId.orElse(null));
    }
}
