package com.zubrilka.zubrilkaenglish.repositories

import android.util.Log
import com.zubrilka.zubrilkaenglish.events.PrEvEnum
import com.zubrilka.zubrilkaenglish.events.PropEvent
import com.zubrilka.zubrilkaenglish.models.PropModel
import com.zubrilka.zubrilkaenglish.repositories.retrofit.RetrofitService
import com.zubrilka.zubrilkaenglish.repositories.room.PropKey
import com.zubrilka.zubrilkaenglish.repositories.room.RoomService
import com.zubrilka.zubrilkaenglish.utils.LOG
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
    private val retrofitService: RetrofitService = RetrofitService()
    private val USERS_LAST_ENTER_key = "USERS_LAST_ENTER"
    private val IS_DEFAULT_MEMO_WAS_DELETED_key = "IS_DEFAULT_MEMO_WAS_DELETED"

    val properties: MutableStateFlow<Map<String,String>> = MutableStateFlow(mapOf())


    init {
        EventBus.getDefault().register(this)

        setupProperties()
    }

    //займется списком всех проперти
    private fun setupProperties() {
        GlobalScope.launch {
            getAllProperties().collect{
                properties.value = it
                GlobalScope.launch {
                    isFirtEntryInApp()
                }
            }
        }
    }

    /**
     * вызывает некоторый код при первой згрузке приложения
     * запрашивает с бека первые несколько слов для обучения пользователя
     */
    private val mutex = Mutex()
    private suspend fun isFirtEntryInApp() {
        mutex.withLock {
            if (properties.value[PropKey.IS_FIRST_ENTRY.key] == true.toString()){
                Log.d(LOG,"PropKey.IS_FIRST_ENTRY.key == ${properties.value[PropKey.IS_FIRST_ENTRY.key]}")

                if (CardsRepository.instance.setInitialTrainingWords()){
                    //тут как бы при неудачной попытке получить с сервера список значение в проперти типа это не первый вход в приложение не установиться в fulse
                    roomService.getPropDAO().insertNewProp(PropModel(PropKey.IS_FIRST_ENTRY.key,false.toString()))
                }
            }
        }
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

    /**
     * пользователь согласился с политикой конфеденциальности
     */
    fun userIsAgreedPrivacy() {
        GlobalScope.launch {
            roomService.getPropDAO().insertNewProp(PropModel(PropKey.IS_AGREE_PRIVACY.key,true.toString()))
        }
    }
}