package com.example.zeapp.onlineCompetition;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * занимается хранением пула текущих поединков игроков
 * содержит методы для их добавления удаления и другое
 */
@Component
public class DuelHolder {
    //хранит в себе поединки пользователей
    private static final Map<Long, Duel> duelMap = new HashMap<>();

    private Long numberDuels = 0L; //укажет количество созданных поединков бутет использоваться для создания id поединка

    /**
     * добавим новый поединок
     */
    public void addNewDuel(Duel newDuel) {
        newDuel.setId(numberDuels);
        numberDuels++;
        duelMap.put(newDuel.getId(), newDuel);
    }
}
