package com.example.zeapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Для передачи пользователю списком для просмотра таблицы рейтинга
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDTO {
    private String short_name; //имя взятое из обьекта Person
    private Integer place; //место в таблице
    private Long personId;
    private Long points; //заработанные очки
    private String lastEntry; //последний вход пользователя
    private Integer newPoints; //заработанные очки за последние сутки
}