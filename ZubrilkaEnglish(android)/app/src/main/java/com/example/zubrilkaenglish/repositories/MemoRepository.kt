package com.example.zubrilkaenglish.repositories

import com.example.zubrilkaenglish.events.MemoEvent
import com.example.zubrilkaenglish.events.MmEvEnum
import com.example.zubrilkaenglish.models.Memo
import com.example.zubrilkaenglish.repositories.room.PropKey
import com.example.zubrilkaenglish.services.memoService.MemoNotificationManager
import com.example.zubrilkaenglish.repositories.room.RoomService
import com.example.zubrilkaenglish.utils.DEFAULT_MEMO
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
    private val propRepository = PropRepository.instance
    private val memoNotificationManager = MemoNotificationManager.instance

    init {
        EventBus.getDefault().register(this)
        createNewMemo(DEFAULT_MEMO)
        propRepository.updateUsersLastEnter()
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
            if (memo.id == DEFAULT_MEMO.id && propRepository.isDefaultMemoWasDeleted()){
                return@launch
            }
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
            if (memo.id == DEFAULT_MEMO.id) propRepository.defaultMemoSetDeleted()
        }
    }

    /**
     * выдаст Flow со списком всех Memo
     */
    fun getAllMemos() = roomService.getMemoDAO().getAllMemos()
    suspend fun getMemoById(memoId: Long): Memo? = roomService.getMemoDAO().getMemoById(memoId)

    /**
     * вернет true если пользователь уже создавал напоминания для себя
     */
    suspend fun isUserHasOwnMemos(): Boolean = propRepository.getPropModelByKey(PropKey.isUserHasOwnMemos.key)?.value.toBoolean()
}