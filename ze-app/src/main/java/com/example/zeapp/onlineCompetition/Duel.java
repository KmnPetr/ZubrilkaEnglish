package com.example.zeapp.onlineCompetition;

import com.example.zeapp.models.SockMessType;
import com.example.zeapp.models.SocketMessage;
import com.example.zeapp.onlineCompetition.socketDto.DuelInfo;
import com.example.zeapp.onlineCompetition.socketDto.FinishInfo;
import com.example.zeapp.onlineCompetition.socketDto.NextWord;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * класс игровой поединок для двух игроков
 * хранит в себе игроков а также другую информацию о текущем поединке
 */
@Getter
@Setter
public class Duel {
    private Long id;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final Integer countPlayers = 2;
    List<Player> players = new ArrayList<>(countPlayers);
    private ArrayList<ComplexWord> duelsListWords; //список предлагаемый обоим пользователям для поединка
    private int curWordPos = -1; //указывает на текущее слово в игре, инкрементируется при раздаче слова
    private Long timeToNextWord; //время в будущем когда можно будет разослать следующее слово игрокам, чтобы рассылка была с небольшой задержкой

    /**
     * проверит, является ли текущее слово последним в списке
     */
    public boolean isWordsEnded() {
        return curWordPos >= (duelsListWords.size() - 1);
    }

    /**
     * выдаст следующее слово по позиции списка
     * а также некоторую другую информацию
     */
    public NextWord getNextWord() {
        curWordPos++;
        NextWord nextWord = null;
        try{
            nextWord = new NextWord(
                    (long) duelsListWords.get(curWordPos).getWord().getId(),
                    curWordPos,
                    duelsListWords.size(),
                    duelsListWords.get(curWordPos).getListAnswers()
            );
        }catch (Exception e){e.printStackTrace();}
        return nextWord;
    }
    /**
     * установит время в будущее когда можно будет разослать следующее слово игрокам
     */
    public void setNewTimeNextWord(){
        timeToNextWord = System.currentTimeMillis()+1000;
    }
    /**
     * проверит, настало ли время разослать следующее слово игрокам
     */
    public boolean isTimeNextWord() {
        return System.currentTimeMillis() > timeToNextWord;
    }
    /**
     * инкрементирует поле countReplies в текущем ComplexWord
     * и проверит все ли участники поединка дали ответ на текущий вопрос
     */
    public boolean incrementAndIsFullCountReplies() {
        int countReplies = duelsListWords.get(curWordPos).getCountReplies().incrementAndGet();
        return countReplies == players.size();
    }
    /**
     * положит id поединка не только в поле самого поединка но и каждому игроку
     */
    public void setId(Long id) {
        this.id = id;
        players.forEach(it->it.setCurrentDuelId(id));
    }

    /**
     * выдаст id текущего слова
     */
    public Long getCurWordId() {
        Long curWordId = null;
        try {
            curWordId = (long) duelsListWords.get(curWordPos).getWord().getId();
        }catch (Exception e){e.printStackTrace();}
        return curWordId;
    }
    /**
     * выдаст позицию текущего правильного ответа
     */
    public int getRightAnswer() {
        Integer rightAnsw = null;
        try {
            rightAnsw = duelsListWords.get(curWordPos).getRightAnswer();
        }catch (Exception e){e.printStackTrace();}
        return rightAnsw;
    }
    /**
     * добавит пользователя в поединок
     * а также добавит ссылку дуели в пользователя
     */
    public void addPlayers(Player player) {
        if(players.size() < countPlayers){
            players.add(player);
        }
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

    /**
     * вернет список активных карточек всех юзеров поединка
     */
    public List<List<Long>> getUsersList() {
        List<List<Long>> listUserWords = new ArrayList<>(players.size());

        players.forEach(player -> {
            listUserWords.add(player.getListActiveCards());
        });
        return listUserWords;
    }


    /**
     * отошлет первоначальную информацию при старте поединка
     * она содержит в себе информацию для каждого игрока персонально о его id
     * и всю информацию по поединку
     */
    public void sendStartInfo() {
        HashMap<String,String> map = new HashMap<>();
        DuelInfo duelInfo = new DuelInfo(this);

        for (int i = 0; i < players.size(); i++) {
            duelInfo.setOwnId(players.get(i).getId());
            duelInfo.setOwnPosition(i);
            map.put("duelInfo",duelInfo.toJson());
            players.get(i).sendMessage(new SocketMessage(SockMessType.START_INFO,map));
        }
    }

    public FinishInfo getFinishInfo() {
        return new FinishInfo();
    }
}