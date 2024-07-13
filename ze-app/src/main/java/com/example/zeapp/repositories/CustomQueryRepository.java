package com.example.zeapp.repositories;

import com.example.zeapp.models.StatisticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
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
                        "WHERE statistics.person_id = 1600")
                .map((row, rowMetadata) -> new StatisticsDTO(
                        row.get("name",String.class),
                        null,
                        row.get("person_id",Long.class),
                        row.get("points",Long.class),
                        DateTimeFormatter.ofPattern("dd-MM-yyyy").format(Objects.requireNonNull(row.get("last_entry", LocalDateTime.class))),
                        row.get("new_points",Integer.class)
                )).one();
    }
}
