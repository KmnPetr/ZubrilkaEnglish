package com.example.WordsManager;

import com.example.WordsManager.models.PropModel;
import com.example.WordsManager.repositories.WordsRepository;
import com.example.WordsManager.services.BackupWordService;
import com.example.WordsManager.services.PropService;
import com.example.WordsManager.services.VoiceFileService;
import com.example.WordsManager.services.WordsService;
import com.example.WordsManager.services.sortingService.WordSorter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * эта точка входа, предоставляет функционал по сохранению обьектов Voice и Word из локального хранилища в БД
 * перед сохранением ведется просчет поля sorting_value у обьектов Word
 */
@SpringBootApplication
@Slf4j
public class SavingDatabaseApplication {
    private final VoiceFileService voiceFileService;
    private final WordsService wordsService;
    private final PropService propService;
    private final BackupWordService backupWordService;
    @Autowired
    public SavingDatabaseApplication(VoiceFileService voiceFileService, WordsService wordsService, PropService propService, BackupWordService backupWordService) {
        this.voiceFileService = voiceFileService;
        this.wordsService = wordsService;
        this.propService = propService;
        this.backupWordService = backupWordService;
    }


    public static void main(String[] args){
        ApplicationContext context = SpringApplication.run(SavingDatabaseApplication.class,args);
        SavingDatabaseApplication thisClass = context.getBean(SavingDatabaseApplication.class);

        //сохранит новые файлы voice в БД
        thisClass.voiceFileService.saveNewVoicesToDB();
        //сохранит или обновит Words в БД
        thisClass.wordsService.saveOrUpdateWords();
        //просто поменяет версию словаря без подсчета всех слов
//        thisClass.increaseDictionaryVersion();
        //пересчитает все слова
        countAllWords(context);


        //забэкапим новое состояние словаря
        thisClass.backupWordService.saveWords();
    }

    /**
     * просто поменяет версию словаря
     */
    private void increaseDictionaryVersion() {
        propService.increaseDictionaryVersion().block();
        PropModel version = propService.getDictionaryVersion().block();
        log.info("Новая версии словаря: {}",version.getValue());
    }


    /**
     * запросит слова все слова из БД и пересчитает у них поле sorting_value
     * затем сохранит обратно в БД
     */
    private static void countAllWords(ApplicationContext context) {
        //замер времени
        long startTime = System.currentTimeMillis();

        //Подготовка
        //Перекачиваем книги из репы в массивы, составляем мапу по словам
        //Запрашиваем из базы все слова
        WordSorter wordSorter = context.getBean(WordSorter.class);
        WordsRepository wordsRepository = context.getBean(WordsRepository.class);

        //установим список слов к обработке
        wordSorter.setWordList(wordsRepository.findAll().collectList().block());

        //переводим count из мапы в список из БД
        wordSorter.setCount();

        //вывод количества слов(фраз) неудачников
        wordSorter.printNumberWordsWithZeroCount();

        //отправляем неудачников на повторный длительный поиск
        wordSorter.searchPhrases(50);

        //еще раз переводим count из мапы в список из БД
        wordSorter.setCount();

        //пересчитаем частоупотребимость неправильных глаголов
        wordSorter.countIrregularVerbs();

        //выводим на экран оставшиеся редкоупотребляемые слова
        wordSorter.printAllWordsWithCountBelowX(50);

        //закоментируй следующие две строки если не уверен
        wordSorter.updateDateInBD();//отправит данные в БД
        wordSorter.increaseDictionaryVersion();//увеличиваем значение версии словаря в бд

        //замер времени
        System.out.println("Время выполнения операции: "+(System.currentTimeMillis()-startTime)+" милисек.");
    }
}
