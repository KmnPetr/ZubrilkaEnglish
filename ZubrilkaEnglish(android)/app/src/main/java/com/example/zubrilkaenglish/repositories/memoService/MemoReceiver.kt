package com.example.zubrilkaenglish.repositories.memoService

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.zubrilkaenglish.models.DayOfWeek
import com.example.zubrilkaenglish.models.Memo
import com.example.zubrilkaenglish.repositories.MemoRepository
import com.example.zubrilkaenglish.utils.LOG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class MemoReceiver : BroadcastReceiver() {

    private val memoRepository = MemoRepository.instance
    private val memoNotificationManager = MemoNotificationManager.instance

    override fun onReceive(context: Context, intent: Intent) {
        val memoId = intent.getLongExtra("memo_id",-1)

        GlobalScope.launch {
            val memo: Memo? = memoRepository.getMemoById(memoId)
            withContext(Dispatchers.Main){
                if (memo != null) {
                    if (checkingDayOfWeek(memo)){
                        memoNotificationManager.showMemoNotification(memo,context)
                    }
                } else {
                    memoNotificationManager.cancelAlarm(context,memoId.toInt())
                }
            }
        }
        Log.d(LOG,"Alarm intent: memoId: $memoId")
    }


    private fun checkingDayOfWeek(memo: Memo):Boolean{

        if (memo.daysOfWeek.contains(DayOfWeek.DAILY)){
            return true
        } else {
            val calendar = Calendar.getInstance()
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            memo.daysOfWeek.forEach {
                if (it.weekDay == dayOfWeek) {
                    println("день недели совпал: $dayOfWeek день: ${it.ruStr}")
                    return true
                }
            }
        }
        println("не совпало ничего")
        return false
    }
}