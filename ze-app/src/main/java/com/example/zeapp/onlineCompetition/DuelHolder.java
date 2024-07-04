package com.example.zeapp.onlineCompetition;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * занимается хранением пула текущих поединков игроков
 * содержит методы для их добавления удаления и другое
 */
@Component
public class DuelHolder {
    //хранит в себе поединки пользователей
    private static final Map<Long, Duel> duelMap = new ConcurrentHashMap<>();

    private Long numberDuels = 0L; //укажет количество созданных поединков будет использоваться для создания id поединка

    /**
     * выдаст список duelMap
     */
    public Map<Long, Duel> getDuels() {
        return duelMap;
    }
    /**
     * добавим новый поединок
     */
    public void addNewDuel(Duel newDuel) {
        newDuel.setId(numberDuels);
        numberDuels++;
        duelMap.put(newDuel.getId(), newDuel);
    }

    /**
     * выдаст Duel по его id
     */
    public Duel getDuelById(Long currentDuelId) {
        return duelMap.get(currentDuelId);
    }

    /**
     * удалит поединок из пула
     */
    public void remove(Duel duel) {
        duelMap.remove(duel.getId());
    }

}
