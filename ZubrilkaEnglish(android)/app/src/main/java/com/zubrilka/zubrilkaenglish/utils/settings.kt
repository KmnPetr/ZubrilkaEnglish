package com.zubrilka.zubrilkaenglish.utils

import com.zubrilka.zubrilkaenglish.models.DayOfWeek
import com.zubrilka.zubrilkaenglish.models.Memo
import com.zubrilka.zubrilkaenglish.screens.training.Modes

//const val URL:String="https://zubrilka-english.com:8443"
const val test_url = "http://192.168.209.182:28700"
const val prod_url:String="https://zubrilka-english.com:8443"
const val privacy_url:String="https://zubrilka-english.com:8443/privacy" //адрес сайта с политикой конфиденциальности
const val URL:String= prod_url

var numAnsForSleep:Int = 5

//используемый формат для записи времени засыпания и пробуждения карточек
val SIM_FORM_DATE: String = "dd.MM.yyyy"

val LOG = "ZE_LOG"


val DEFAULT_MEMO = Memo(
    1_000_000,
    12,
    0,
    "\tПовторяйте слова чаще, чтобы лучше их запоминать. \n Ваш словарный запас - ваша уверенность владения языком!",
    listOf(DayOfWeek.DAILY)
)

val APP_NAME:String = "Zubrilka English"
val APP_EMAIL:String = "zubrilka.en@gmail.com"

val LIMIT_ACTIVE_CARDS:Int = 70


val defaultMode = Modes.multipleChoice

val  isVibrationEnabled: Boolean = true

val delayFlipping_0:Long = 700L //задержка перелистывания при режиме обучения "На честность"
val delayFlipping_1:Long = 2000L //задержка перелистывания при режиме обучения "Многовариантный выбор"
val delayFlipping_3:Long = 700L //при короткой задержке, когда юзер ответил правильно в режиме "Многовариантный выбор"