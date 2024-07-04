package com.example.zubrilkaenglish.screens.competition

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.events.CmpEvEnum
import com.example.zubrilkaenglish.events.CompetitionEvent
import com.example.zubrilkaenglish.models.Profile
import com.example.zubrilkaenglish.models.socketDto.DuelInfo
import com.example.zubrilkaenglish.models.socketDto.FinishInfo
import com.example.zubrilkaenglish.models.socketDto.NextWord
import com.example.zubrilkaenglish.models.socketDto.StatusInfo
import com.example.zubrilkaenglish.onlineCompetition.CompetitionManager
import com.example.zubrilkaenglish.repositories.CardsRepository
import com.example.zubrilkaenglish.repositories.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class CompetitionViewModel : ViewModel() {
    private val competitionManager = CompetitionManager.instance
    private val profileRepository = ProfileRepository.instance
    private val cardsRepository = CardsRepository.instance

    val ping: MutableLiveData<Long?> = MutableLiveData(null)
    val duelInfo: MutableLiveData<DuelInfo?> = MutableLiveData(null)
    val profile: MutableLiveData<Profile?> = MutableLiveData(null)
    val startCountDown: MutableLiveData<Int?> = MutableLiveData(null)
    val nextWord: MutableLiveData<NextWord?> = MutableLiveData(null)
    val finishInfo: MutableLiveData<FinishInfo?> = MutableLiveData(null)
    val statusInfo: MutableLiveData<StatusInfo?> = MutableLiveData(null)

    var opponentId: Long? = null //id противника
    var opponentWrongPos:Int? = null //позиция кнопки неправильно отвеченная противником будет подкрашена серым
    val ownHealth: MutableLiveData<Int?> = MutableLiveData(null) //здоровье данного игрока
    val opponentHealth: MutableLiveData<Int?> = MutableLiveData(null) //здоровье данного игрока

    init {
        EventBus.getDefault().register(this)

        //вернет значение ping из NetworkHolder
        viewModelScope.launch{
            competitionManager.ping.collect{
                ping.postValue(it)
            }
        }
        //вернет значение duelInfo из CompetitionManager
        viewModelScope.launch{
            competitionManager.duelInfo.collect{
                duelInfo.postValue(it)
                receiveDuelInfo(it)
            }
        }
        //вернет значение profile из ProfileRepository
        viewModelScope.launch{
            profileRepository.profile.collect{
                profile.postValue(it)
            }
        }
        //вернет значение startCountDown из CompetitionManager
        viewModelScope.launch{
            competitionManager.startCountDown.collect{
                startCountDown.postValue(it)
            }
        }
        //вернет значение nextWord из CompetitionManager
        viewModelScope.launch{
            competitionManager.nextWord.collect{
                opponentWrongPos = null //сбросим значение
                nextWord.postValue(it)
            }
        }
        //вернет значение finishInfo из CompetitionManager
        viewModelScope.launch{
            competitionManager.finishInfo.collect{
                finishInfo.postValue(it)
            }
        }
        //вернет значение finishInfo из CompetitionManager
        viewModelScope.launch{
            competitionManager.statusInfo.collect{
                statusInfo.postValue(it)
            }
        }
    }

    /**
     * при получении DuelInfo настраивает некоторые поля
     */
    private fun receiveDuelInfo(duelInfo: DuelInfo?) {
        opponentId = getOpponentId(duelInfo)

        if (duelInfo != null) {
            if (duelInfo.ownPosition == 0){
                ownHealth.value = duelInfo.listHealth[0]
                opponentHealth.value = duelInfo.listHealth[1]
            } else if (duelInfo.ownPosition == 1){
                ownHealth.value = duelInfo.listHealth[1]
                opponentHealth.value = duelInfo.listHealth[0]
            }
        } else{
            ownHealth.value = null
            opponentHealth.value = null
        }
    }

    /**
     * метод используется библиотечкой EventBus
     * для прослушивания запросов от различных view
     */
    @Subscribe
    fun subscribeOnCompetitionEvent(event: CompetitionEvent){
        when(event.typeEvent){
            CmpEvEnum.PEN_WAIT -> receivePenaltyWaiting(event)
            else -> {}
        }
    }

    /**
     * вызывается при получении сообщении о штрафе за чрезмерную задержку времени
     */
    private fun receivePenaltyWaiting(event: CompetitionEvent) {
        if (event.properties["idPlayer"].toString().toLong() == (profile.value?.id ?: false)){ //значит штраф пришел на этого игрока
            ownHealth.value = event.properties["newHealth"].toString().toInt()
        }
        if (event.properties["idPlayer"].toString().toLong()==opponentId){ //значит штраф пришел на противника
            opponentHealth.value = event.properties["newHealth"].toString().toInt()
        }
    }

    /**
     * вычленит id противника
     */
    private fun getOpponentId(duelInfo: DuelInfo?): Long? {
        if (duelInfo!=null){
            if (duelInfo.ownPosition==0) return duelInfo.listId[1]
            else if (duelInfo.ownPosition==1) return duelInfo.listId[0]
            else return null
        }else return null
    }
}