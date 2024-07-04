package com.example.zubrilkaenglish.utils

import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.CardsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import org.tartarus.snowball.SnowballProgram
import org.tartarus.snowball.ext.EnglishStemmer
import org.tartarus.snowball.ext.RussianStemmer

/**
 * класс дает результат по поиску WordCard из БД по запросу из поисковой строки
 */
class SearchObject private constructor(){
    companion object{
        val instance: SearchObject by lazy {
            SearchObject().apply { CoroutineScope(Dispatchers.Default).launch {
                //TODO класс может давать сбои, если нужные данные не подгрузились из сети, особенно при первом запуске приложения
                listAllWordCard = CardsRepository.instance.getAllWordCards()
            }
            }
        }
    }
    private var listAllWordCard = emptyList<WordCard>()
    private val englishStemmer = EnglishStemmer()
    private val russianStemmer = RussianStemmer()

    val listSearchWords: MutableStateFlow<List<WordCard>> = MutableStateFlow(emptyList()) //сюда будут складываться найденные слова

    private var searchJob: Job? = null

    //делаем поиск в корутине чтобы не замедлять основной поток
    fun search(word: String){
        GlobalScope.launch {
            searchJob?.cancelAndJoin() //если пользователь пишет несколько букв подряд чтобы на каждую не создавался джоб
            // Запускаем корутину в контексте текущего (главного) потока
            searchJob = GlobalScope.launch {
                search2(word)
            try {
            } catch (e: Exception) {
                println("Ошибка в корутине поиска слова")
            }
            }
        }
    }
    //выполнит поиск
    private fun search2(word: String) {

        //список для слов с точным совпадением
        val firstList = ArrayList<WordCard>()
        //список для слов с совпадением по корню
        val secondList = ArrayList<WordCard>()
        //список для слов с примерным совпадением по алгоритму Левенштейна
        val thirdList = ArrayList<WordCard>()
        //список для слов с совпадением комбинации символов (типа "ain" в слове "contains")
        val fourthList = ArrayList<WordCard>()

        if (isEnglish(word)) {
            listAllWordCard.forEach {
                if (word == it.word.foreignWord) {
                    firstList.add(it)
                } else if (comparisonByRoot(word, it.word.foreignWord!!, englishStemmer)) {
                    secondList.add(it)
                } else if (levenshteinDistance(word, it.word.foreignWord)) {
                    thirdList.add(it)
                }else if (it.word.foreignWord.contains(word)) {
                    fourthList.add(it)
                }
            }
        } else if (isRussian(word)) {
            listAllWordCard.forEach {
                if (word == it.word.translation) {
                    firstList.add(it)
                } else if (comparisonByRoot(word, it.word.translation!!, russianStemmer)) {
                    secondList.add(it)
                } else if (levenshteinDistance(word, it.word.translation)) {
                    thirdList.add(it)
                }else if (it.word.translation.contains(word)) {
                    fourthList.add(it)
                }
            }
        }

        val list = ArrayList<WordCard>()
        firstList.forEach { list.add(it) }
        secondList.forEach { list.add(it) }
        thirdList.forEach { list.add(it) }
        fourthList.forEach { list.add(it) }

        listSearchWords.value = list
        //криво косо работает
    }

    /**
     * функция сравнит слова по Левенштейну, минимальному количеству операций для приведения одного слова к другому
     */
    private fun levenshteinDistance(word: String,word2: String): Boolean {
        val levenshteinDistance = StringUtils.getLevenshteinDistance(word,word2)
        if(levenshteinDistance<=1) return true
        return false
    }

    private fun comparisonByRoot(word: String,word2: String, stemmer: SnowballProgram): Boolean {
        englishStemmer.current = word
        //приводим к корню
        //иногда надо несколько раз приводить к корню если приставок несколько
        englishStemmer.stem()
        englishStemmer.stem()
        englishStemmer.stem()
        val firstStemm: String = englishStemmer.current

        englishStemmer.current = word2
        englishStemmer.stem()
        englishStemmer.stem()
        englishStemmer.stem()
        val secondStemm: String = englishStemmer.current

        if (firstStemm==secondStemm)return true
        return false
    }

    private fun isRussian(text: String): Boolean {
        val russianCharacters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
        return text.filter { russianCharacters.contains(it.lowercaseChar()) }.length > 0
    }
    private fun isEnglish(text: String): Boolean {
        val englishCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        return text.filter { englishCharacters.contains(it) }.length > 0
    }

    //очиистиит список найденных слов
    fun clearList() {
        listSearchWords.value = emptyList()
    }
}