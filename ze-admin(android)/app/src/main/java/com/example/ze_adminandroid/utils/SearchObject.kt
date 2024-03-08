package com.example.ze_adminandroid.utils

import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.repositories.WordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
                listAllWordCard = WordRepository.instance.getAllWords()
            }
            }
        }
    }
    private var listAllWordCard = emptyList<Word>()
    private val englishStemmer = EnglishStemmer()
    private val russianStemmer = RussianStemmer()

    fun search(word: String): List<Word> {

        //список для слов с точным совпадением
        val firstList = ArrayList<Word>()
        //список для слов с совпадением по корню
        val secondList = ArrayList<Word>()
        //список для слов с примерным совпадением по алгоритму Левенштейна
        val thirdList = ArrayList<Word>()

        if (isEnglish(word)) {
            listAllWordCard.forEach {
                if (word == it.foreignWord) {
                    firstList.add(it)
                } else if (comparisonByRoot(word, it.foreignWord!!, englishStemmer)) {
                    secondList.add(it)
                } else if (levenshteinDistance(word, it.foreignWord)) {
                    thirdList.add(it)
                }
            }
        } else if (isRussian(word)) {
            listAllWordCard.forEach {
                if (word == it.translation) {
                    firstList.add(it)
                } else if (comparisonByRoot(word, it.translation!!, russianStemmer)) {
                    secondList.add(it)
                } else if (levenshteinDistance(word, it.translation)) {
                    thirdList.add(it)
                }
            }
        }

        val list = ArrayList<Word>()
        firstList.forEach { list.add(it) }
        secondList.forEach { list.add(it) }
        thirdList.forEach { list.add(it) }
        return list
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
}