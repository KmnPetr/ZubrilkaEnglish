package com.example.bookAnalyzer.bd_service;

import com.example.bookAnalyzer.models.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class WordServise {
    private final WordRepository wordRepository;
    private final PropRepository propRepository;

    @Autowired
    public WordServise(WordRepository wordRepository, PropRepository propRepository) {
        this.wordRepository = wordRepository;
        this.propRepository = propRepository;
    }

    public List<Word> getAllWordsFromBd(){
        return wordRepository.findAll();
    }

    @Transactional
    public void updateDateInBD(List<Word> wordList){
        int i = 0;
        for (Word w : wordList) {
            wordRepository.updateDateInBD(w.getSorting_value(),w.getId());

            if ((i%300)==0){
                //выполнение метода долгое, поэтому сделаем некий вывод в консоль
                int percent = (int) ((double) i/ wordList.size()*100);
                System.out.println("Отправка в БД.. "+percent+"%");
            }
            i++;
        }
    }

    /**
     * метод установит новое значение update_at в таблице properties
     */
    @Transactional
    public void setUpdateAt() {
        propRepository.setNewValue("update_at", ZonedDateTime.now().toString());
    }

}
