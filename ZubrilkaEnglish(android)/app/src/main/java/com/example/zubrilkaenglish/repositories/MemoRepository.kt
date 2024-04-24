package com.example.zubrilkaenglish.repositories

import com.example.zubrilkaenglish.events.MemoEvent
import com.example.zubrilkaenglish.events.MmEvEnum
import com.example.zubrilkaenglish.models.Memo
import com.example.zubrilkaenglish.repositories.memoService.MemoNotificationManager
import com.example.zubrilkaenglish.repositories.room.RoomService
import com.example.zubrilkaenglish.utils.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MemoRepository private constructor() {
    companion object {
        val instance: MemoRepository by lazy { MemoRepository() }
    }
    private val roomService:RoomService = RoomService()
    private val memoNotificationManager = MemoNotificationManager.instance
    init {
        EventBus.getDefault().register(this)
    }

    /**
     * метод используется библиотечкой EventBus
     * для прослушивания запросов от различных view
     */
    @Subscribe
    fun subscribeOnMemoEvent(event: MemoEvent){
        when(event.typeEvent){
            MmEvEnum.DELETE_MEMO -> {
                    deleteMemo(event.memo)
            }
            MmEvEnum.CREATE_MEMO -> {
                createNewMemo(event.memo)
            }
            else -> {}
        }
    }

    /**
     * создаст memo
     * TODO еще должен добавить в AlarmManager
     */
    private fun createNewMemo(memo: Memo) {
        GlobalScope.launch {
            val memoId:Long = roomService.getMemoDAO().insertNewMemo(memo)
            val savedMemo = roomService.getMemoDAO().getMemoById(memoId)
            withContext(Dispatchers.Main){
                if (savedMemo != null) {
                    memoNotificationManager.setupAlarm(savedMemo,MyApplication.context)
                }
            }
        }
    }

    /**
     * удалит memo
     * TODO еще должен удалить из AlarmManager
     */
    private fun deleteMemo(memo: Memo) {
        GlobalScope.launch {
            roomService.getMemoDAO().deleteMemo(memo)
            memoNotificationManager.cancelAlarm(MyApplication.context,memo.id.toInt())
        }
    }

    /**
     * выдаст Flow со списком всех Memo
     */
    fun getAllMemos() = roomService.getMemoDAO().getAllMemos()
    suspend fun getMemoById(memoId: Long): Memo? = roomService.getMemoDAO().getMemoById(memoId)
}