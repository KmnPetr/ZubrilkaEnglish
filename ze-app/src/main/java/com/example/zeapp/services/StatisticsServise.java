package com.example.zeapp.services;

import com.example.zeapp.models.Statistics;
import com.example.zeapp.models.StatisticsDTO;
import com.example.zeapp.repositories.CustomQueryRepository;
import com.example.zeapp.repositories.PersonRepository;
import com.example.zeapp.repositories.StatisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Сервис занимается обработкой данных по статистике пользователей
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class StatisticsServise {

    private final StatisticsRepository statisticsRepository;
//    private final PersonRepository personRepository; //TODO временно
//    private final PasswordEncoder passwordEncoder; //TODO временно
    private final CustomQueryRepository customQueryRepository;
    private Map<Long,StatisticsDTO> cacheStats = new ConcurrentHashMap<>();

    @Autowired
    public StatisticsServise(StatisticsRepository statisticsRepository, PersonRepository personRepository, PasswordEncoder passwordEncoder, CustomQueryRepository customQueryRepository) {
        this.statisticsRepository = statisticsRepository;
        this.customQueryRepository = customQueryRepository;
//        this.personRepository = personRepository;
//        this.passwordEncoder = passwordEncoder;

//        Mono.just("").delayElement(Duration.ofSeconds(30)).subscribe(it->generateTestList());

        Flux.interval(Duration.ZERO, Duration.ofMinutes(1)).onBackpressureBuffer(1).doOnNext(tick -> replaceCacheMap()).subscribe();
        Flux.interval(Duration.ofHours(1)).onBackpressureBuffer(1).doOnNext(tick -> scheduleTask()).subscribe();
    }

    /**
     * таска запускается раз в день
     * уменьшает на определенный процент поле statistics.points
     * если пользователь не обновлял статистику последние определенное количество дней
     * в пределах 8-14 дня уменьшаем на 5%
     * в пределах 15-21 дня уменьшаем на 2%
     * в пределах 22-100 дня уменьшаем на 1%
     * свыше 100 дней обнуляем
     */
    @Scheduled(cron = "0 0 4 * * *")
    public void scheduleTask2() {
        //логика расчета времени будет реализована в sql коде
        customQueryRepository
                .reducePoints(8,14,0.95)
                .subscribe(updatedRows -> log.info("ежедневное обновление поля statistics.points: изменено {} строк.",updatedRows));
        customQueryRepository
                .reducePoints(15,21,0.98)
                .subscribe(updatedRows -> log.info("ежедневное обновление поля statistics.points: изменено {} строк.",updatedRows));
        customQueryRepository
                .reducePoints(22,100,0.99)
                .subscribe(updatedRows -> log.info("ежедневное обновление поля statistics.points: изменено {} строк.",updatedRows));
        customQueryRepository
                .clearPoints(100)
                .subscribe(updatedRows -> log.info("ежедневная очистка поля statistics.points: обнулено {} строк.",updatedRows));
    }

    /**
     * таска запускается каждый час
     * проверяет статистику пользователей
     * если пользователь не заходил 8 часов, она обнуляет поле statistics.last_entry в БД у пользователя
     */
    private void scheduleTask() {
        //логика расчета времени будет реализована в sql коде
        customQueryRepository
                .checkNewPointsToClear(8)
                .subscribe(
                        updatedRows -> log.info("scheduleTask: Successfully updated rows: {}",updatedRows)
                        , error -> log.info("scheduleTask: Failed to update rows: {}",error.getMessage())
                );
    }

    /**
     * Работает в цикле
     * закеширует новые данные в мапе по списку первых 1500 пользователей с самым высоким points
     */
    private void replaceCacheMap() {
        customQueryRepository
                .getFirst1500users_rating()
                .collectMap(StatisticsDTO::getPersonId, stat -> stat) // Собираем Flux в ConcurrentHashMap
                .doOnNext(map -> cacheStats = new ConcurrentHashMap<>(map)) // Обновляем userMap
                .subscribe();
    }

    /**
     * Выдаст первых 1500 юзеров с наибольшим количеством очков
     * включая один объект статистики пользователя от которого пришел запрос
     */
    public Flux<StatisticsDTO> getFirst1500users_rating(Long ownId) {
        if (ownId == null) return Flux.fromIterable(cacheStats.values());
        return customQueryRepository
                .getStatisticByPersonId(ownId)
                .flatMapMany(ownStatPerson->
                        {
                                cacheStats.put(ownStatPerson.getPersonId(),ownStatPerson);
                                return Flux.fromIterable(cacheStats.values());
                        }
                )
                .switchIfEmpty(
                        Flux.fromIterable(cacheStats.values())
                );
    }

    /**
     * выдаст StatisticsDTO по id юзера
     */
    public Mono<StatisticsDTO> getStatisticByPersonId(Long ownId){
        return customQueryRepository.getStatisticByPersonId(ownId);
    }
//    /**
//     * Создаст тестовый список статистик пользователей
//     */
//    private void generateTestList() {
//
//        System.out.println("1111111111111111111111111111111111111111111111111111111");
//        Integer size = 1500;
//
//
//        ArrayList<Person> listTestPersons = new ArrayList<>(size);
//        for (int i = 0; i < size; i++) {
//            listTestPersons.add(new Person(
//                    null,
//                    "Guest_"+i+"@t.t",
//                    passwordEncoder.encode("password"+i),
//                    "TestGuest_"+i,
//                    UserRole.ROLE_USER,
//                    new Timestamp(System.currentTimeMillis())
//            ));
//
//            if (i%100 == 0) System.out.println(i);
//        }
//        System.out.println("22222222222222222222222222222222222222222222222222222222222");
//
//        personRepository.saveAll(Flux.fromIterable(listTestPersons))
//                .onErrorContinue((ex, obj) -> {
//                    // Логирование ошибки, если это необходимо
//                    System.err.println("Ошибка при сохранении Person: " + obj);
//                })
//                .thenMany(personRepository.findAll())
//                .flatMap(person -> {
//                    Statistics statistics = new Statistics(
//                            null,
//                            (long) person.getId(),
//                            (long) new Random().nextInt(10001),
//                            LocalDateTime.now(),
//                            new Random().nextInt(-1000, 1001)
//                    );
//                    return statisticsRepository.save(statistics);
//                })
//                .onErrorContinue((ex, obj) -> {
//                    // Логирование ошибки, если это необходимо
//                    System.err.println("Ошибка при сохранении Statistics: " + obj);
//                }).subscribe();
//
//        System.out.println("3333333333333333333333333333333333333333333333333333333333333");
//
//
////        System.out.println("LIST TEST STATISTICS SIZE: "+listStatistics.size());
//    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED,readOnly = false)
    public Mono<Statistics> updatePoints(Long personId, Long earnedPoints) {

        if (personId<0) return Mono.empty(); //отсеим ботов с отрицательным id

        System.out.println("updatePoints(Long personId,Long earnedPoints) "+personId+" "+ earnedPoints);

        return statisticsRepository
                .findByPersonId(personId)
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
     * Прибавит или убавит очки,
     * обновит дату
     */
    private Statistics addPointsAndRefreshData(Statistics statistics, Long earnedPoints){

        if (statistics.getPoints()==null)statistics.setPoints(earnedPoints);
        else statistics.setPoints(statistics.getPoints() + earnedPoints);
        if (statistics.getPoints()<0) statistics.setPoints(0L);

        if (statistics.getNewPoints()==null) statistics.setNewPoints(earnedPoints.intValue());
        else statistics.setNewPoints(statistics.getNewPoints() +earnedPoints.intValue());

        statistics.setLastEntry(LocalDateTime.now());

        return statistics;
    }

}
