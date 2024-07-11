package com.example.zubrilkaenglish.utils

import com.example.zubrilkaenglish.models.DayOfWeek
import com.example.zubrilkaenglish.models.Memo
import com.example.zubrilkaenglish.screens.training.Modes

//const val URL:String="https://zubrilka-english.com:8443"
const val test_url = "http://192.168.173.182:28700"
const val prod_url:String="https://zubrilka-english.com:8443"
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

val rewardedAdUnitId:String = "R-M-7862350-2"
val interstitialAdUnitId:String = "R-M-7862350-1"

val defaultMode = Modes.ofHonesty

val  isVibrationEnabled: Boolean = true

val delayFlipping_0:Long = 700L //задержка перелистывания при режиме обучения "На честность"
val delayFlipping_1:Long = 2000L //задержка перелистывания при режиме обучения "Многовариантный выбор"
val delayFlipping_3:Long = 700L //при короткой задержке, когда юзер ответил правильно в режиме "Многовариантный выбор"