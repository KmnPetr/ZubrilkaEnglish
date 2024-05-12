package com.example.zubrilkaenglish.services.memoService

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.zubrilkaenglish.models.DayOfWeek
import com.example.zubrilkaenglish.models.Memo
import com.example.zubrilkaenglish.repositories.MemoRepository
import com.example.zubrilkaenglish.repositories.PropRepository
import com.example.zubrilkaenglish.utils.DEFAULT_MEMO
import com.example.zubrilkaenglish.utils.LOG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class MemoReceiver : BroadcastReceiver() {

    private val memoRepository = MemoRepository.instance
    private val propRepository = PropRepository.instance
    private val memoNotificationManager = MemoNotificationManager.instance

    override fun onReceive(context: Context, intent: Intent) {
        val memoId = intent.getLongExtra("memo_id",-1)

        GlobalScope.launch {
            if (memoId == DEFAULT_MEMO.id){ //логика обработки дефолтного уведомления
                if (isDefaultMemoCanShow()){
                    memoNotificationManager.showMemoNotification(DEFAULT_MEMO,context)
                }
            } else { //логика обработки обычного уведомления
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
        }
        Log.d(LOG,"Alarm intent: memoId: $memoId")
    }

    /**
     * выдаст решение можно ли показывать дефолтовое memo
     * зависит от времени последнего входа пользователя и др.
     */
    private suspend fun isDefaultMemoCanShow(): Boolean {
        val usersLastEnter:Long? = propRepository.getUsersLastEnter()
        return if (usersLastEnter == null) true
        else {
            val different = 129_600_000
            (usersLastEnter+different)<System.currentTimeMillis()
        }
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