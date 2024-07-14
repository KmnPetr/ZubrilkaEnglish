package com.example.zeapp.repositories;

import com.example.zeapp.models.StatisticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Repository
public class CustomQueryRepository{

    private final DatabaseClient databaseClient;
    @Autowired
    public CustomQueryRepository(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }


    /**
     * Выдаст первых 1500 юзеров с наибольшим количеством очков
     */
    public Flux<StatisticsDTO> getFirst1500users_rating() {
        return databaseClient.sql("SELECT person.short_name AS name, " +
                "statistics.person_id, " +
                "statistics.points, " +
                "statistics.last_entry, " +
                "statistics.new_points " +
                "FROM person JOIN statistics " +
                "ON person.id = statistics.person_id " +
                "ORDER BY statistics.points DESC " +
                "LIMIT 1500")
                .map((row, rowMetadata) -> new StatisticsDTO(
                        row.get("name",String.class),
                        null,
                        row.get("person_id",Long.class),
                        row.get("points",Long.class),
                        DateTimeFormatter.ofPattern("dd-MM-yyyy").format(Objects.requireNonNull(row.get("last_entry", LocalDateTime.class))),
                        row.get("new_points",Integer.class)
                )).all();
    }

    /**
     * выдаст StatisticsDTO по id юзера
     */
    public Mono<StatisticsDTO> getStatisticByPersonId(Long ownId) {
        return databaseClient.sql("SELECT " +
                        "person.short_name AS name, " +
                        "statistics.person_id, " +
                        "statistics.points, " +
                        "statistics.last_entry, " +
                        "statistics.new_points " +
                        "FROM person JOIN statistics " +
                        "ON person.id = statistics.person_id " +
                        "WHERE statistics.person_id = "+ownId)
                .map((row, rowMetadata) -> new StatisticsDTO(
                        row.get("name",String.class),
                        null,
                        row.get("person_id",Long.class),
                        row.get("points",Long.class),
                        DateTimeFormatter.ofPattern("dd-MM-yyyy").format(Objects.requireNonNull(row.get("last_entry", LocalDateTime.class))),
                        row.get("new_points",Integer.class)
                )).one();
    }

    /**
     * Почистиит поле statistics.new_points у пользователей, кто не обновлял статистику более {n} часов
     * вернет ответом количество обновленных строк
     */
    public Mono<Long> checkNewPointsToClear(int hours) {
        return databaseClient.sql(
                "UPDATE statistics " +
                "SET new_points = 0 " +
                "WHERE last_entry < NOW() - INTERVAL '"+hours+" hours';"
        )
                .fetch()
                .rowsUpdated();
    }

    /**
     * Уменьшит число statistics.points для записей которые не обновлялись определенный интервал времени
     */
    public Mono<Long> reducePoints(int minDays, int maxDay, double percent) {
        return databaseClient.sql(
                "UPDATE statistics " +
                "SET points = points * "+percent+" " +
                "WHERE last_entry BETWEEN NOW() - INTERVAL '"+maxDay+" days' AND NOW() - INTERVAL '"+minDays+" days';")
                .fetch()
                .rowsUpdated();
    }

    /**
     * Обнулит поле statistics.points которое не обновлялось последние {days} дней
     */
    public Mono<Long> clearPoints(int days) {
        return databaseClient.sql("UPDATE statistics " +
                "SET points = 0 " +
                "WHERE last_entry < NOW() - INTERVAL '"+days+" days';")
                .fetch()
                .rowsUpdated();
    }
}
