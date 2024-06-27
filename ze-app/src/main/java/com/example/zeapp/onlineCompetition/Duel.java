package com.example.zeapp.onlineCompetition;

import com.example.zeapp.models.SocketMessage;
import com.example.zeapp.models.Word;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * класс игровой поединок для двух игроков
 * хранит в себе игроков а также другую информацию о текущем поединке
 */
@Getter
@Setter
public class Duel {
    Long id;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final Integer countPlayers = 2;
    List<Player> players = new ArrayList<>(countPlayers);
    List<Word> words;

    /**
     * добавит пользователя в поединок
     */
    public void addPlayers(Player player) {
        if(players.size() < countPlayers) players.add(player);
    }

    /**
     * укажет достаточно ли игроков в поединке
     */
    public boolean isFull() {
        if (players.size() == countPlayers) return true;
        else return false;
    }

    /**
     * установит статус игроков как занятые
     * здесь это сделать удобнее всего
     */
    public void setPlayersBusy() {
            players.forEach(it->{
                if (it!=null)//игрок мог уже выйти
                        it.setIsBusy(true);
                else setPlayersNotBusy();
                    }
            );
    }

    /**
     * установит статус игроков как незанятые
     */
    private void setPlayersNotBusy() {
        players.forEach(it->{if(it!=null) it.setIsBusy(false);});
    }

    /**
     * отошлет сообщение всем участникам поединка
     */
    public void sendToAllPlayers(SocketMessage socketMessage) {
        try{
            players.forEach(player -> player.sendMessage(socketMessage));
        }catch (Exception ignore){}//на всякий вдруг null какие будут
    }
}
