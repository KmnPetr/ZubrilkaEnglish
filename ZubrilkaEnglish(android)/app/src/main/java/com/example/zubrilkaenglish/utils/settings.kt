package com.example.zubrilkaenglish.utils

import com.example.zubrilkaenglish.models.DayOfWeek
import com.example.zubrilkaenglish.models.Memo

const val URL:String="https://598224.cloud4box.ru"
const val LOCAL_URL:String="http://192.168.40.182:28800"

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