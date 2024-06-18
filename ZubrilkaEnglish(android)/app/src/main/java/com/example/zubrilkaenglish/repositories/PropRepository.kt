package com.example.zubrilkaenglish.repositories

import com.example.zubrilkaenglish.events.PrEvEnum
import com.example.zubrilkaenglish.events.PropEvent
import com.example.zubrilkaenglish.models.PropModel
import com.example.zubrilkaenglish.repositories.room.RoomService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

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




    init {
        EventBus.getDefault().register(this)
    }

    /**
     * метод используется библиотечкой EventBus
     * для прослушивания запросов от различных view
     */
    @Subscribe
    fun subscribeOnPropEvent(event: PropEvent){
        when(event.typeEvent){
            PrEvEnum.UPDATE_REQUEST -> {
                updateOrCreateProperties(event.propModel)
            }
        }
    }

    /**
     * обновит или создаст новый проперти в БД
     */
    private fun updateOrCreateProperties(propModel: PropModel) {
        GlobalScope.launch {
            roomService.getPropDAO().updateOrCreateProperties(propModel)
        }
    }

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
    /**
     * выдаст Flow со списком всех Properties
     */
    fun getAllProperties(): Flow<Map<String, String>> = roomService
        .getPropDAO()
        .getAllProperties()
        .map { list -> list.associate { propModel -> propModel.key to propModel.value } }

    /**
     * вернет проперти из БД по ключу
     */
    suspend fun getPropModelByKey(key: String): PropModel? = roomService.getPropDAO().getPropByKey(key)
}