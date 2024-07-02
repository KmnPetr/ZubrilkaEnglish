package com.example.zeapp.onlineCompetition.socketDto;


import com.example.zeapp.onlineCompetition.Duel;
import com.example.zeapp.onlineCompetition.Player;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * класс содержит информацию о данном текущем поединке
 * предназначен для более гибкого парсинга в json и отправки пользователю
 * порядок положения в списках id, имен, и других полей должно строго следовать порядку положения игроков в их списке
 */
@Getter
@Setter
public class DuelInfo{
    private Long ownId; //укажет id игрока к которому отправляется сообщение
    private Integer ownPosition; //укажет позицию конкретного игрока в списках, чтобы на фронте легче разбираться
    private List<Long> listId;
    private List<String> listShortNames;
    private List<Integer> listHealth;

    public DuelInfo(Duel duel){
        listId = new ArrayList<>(duel.getPlayers().size());
        listShortNames = new ArrayList<>(duel.getPlayers().size());
        listHealth = new ArrayList<>(duel.getPlayers().size());

        for (Player player: duel.getPlayers()){
            listId.add(player.getId());
            listShortNames.add(player.getPerson().getShort_name());
            listHealth.add(player.getHealth());
        }
    }

    // Метод для преобразования объекта DuelInfo в JSON
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
