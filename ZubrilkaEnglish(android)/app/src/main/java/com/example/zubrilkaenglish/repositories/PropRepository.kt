package com.example.zubrilkaenglish.repositories

import com.example.zubrilkaenglish.models.PropModel
import com.example.zubrilkaenglish.repositories.room.RoomService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * следит за проперти значениями в БД и на серваке наверное
 */
class PropRepository private constructor(){
    companion object{
        val instance: PropRepository by lazy { PropRepository() }
    }
    private val roomService: RoomService = RoomService()
    private val USERS_LAST_ENTER_key = "USERS_LAST_ENTER"
    private val IS_DEFAULT_MEMO_WAS_DELETED_key = "IS_DEFAULT_MEMO_WAS_DELETED"

    /**
     * обновит время последнего входа пользователя в приложение
     */
    fun updateUsersLastEnter() {
        val newLastEnter = PropModel(USERS_LAST_ENTER_key,System.currentTimeMillis().toString())
        GlobalScope.launch {
            roomService.getPropDAO().insertNewProp(newLastEnter)
        }
    }

    /**
     * вернет время последнего входа пользователя в приложение
     */
    suspend fun getUsersLastEnter(): Long? {
        return roomService.getPropDAO().getPropByKey(USERS_LAST_ENTER_key)?.value?.toLong()
    }

    /**
     * проверит, удалял ли пользователь дефолтное memo
     */
    suspend fun isDefaultMemoWasDeleted(): Boolean {
        return roomService.getPropDAO().getPropByKey(IS_DEFAULT_MEMO_WAS_DELETED_key)?.value.toBoolean()
    }

    suspend fun defaultMemoSetDeleted() {
        roomService.getPropDAO().insertNewProp(
            PropModel(IS_DEFAULT_MEMO_WAS_DELETED_key,true.toString())
        )
    }
}