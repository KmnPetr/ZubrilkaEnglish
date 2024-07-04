package com.example.zeapp.onlineCompetition.socketDto;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * обьект дто отправится игроку с сервера в ответ на его клик по одному из вариантов ответов
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClickResult {
    private Long idPlayer; //id игрока сделавшаго выбор
    private Integer newHealth; //новое значение здоровья игрока, улучшилось ли оно или ухудшилось вычисляется на фронте
    private Long idWord; //id слова на всякий случай чтоб предотвратить разные временные баги
    private Boolean isRight; //правилен ли был выбор игрока
    private Integer rightPos; //позиция правильного ответа, если отправляется противнику игрока сделавшего выбор, то ставится "null"
    private Integer wrongPos; //позиция неправильно сделанного ответа, чтоб не хранить на фронте, отсылается всем участникам

    // Метод для преобразования объекта DuelInfo в JSON
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
