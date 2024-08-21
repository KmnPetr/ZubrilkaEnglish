package com.example.zeapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Класс ентити содержит различные статистические данные объекта Person
 */
@Table("statistics")
@Data
@AllArgsConstructor
public class Statistics {
    @Id
    private Long id;
    @Column("person_id")
    private Long personId;
    @Column("points")
    private Long points; //заработанные очки
    @Column("last_entry")
    private LocalDateTime lastEntry; //последний вход пользователя
    @Column("new_points")
    private Integer newPoints; //заработанные очки за последние сутки
    @Column("offline_points")
    private Integer offlinePoints; //заработанные очки за последние сутки в офлайн режимах, чтобы нельзя было заработать на офлайн режимах читерно много очков
}
