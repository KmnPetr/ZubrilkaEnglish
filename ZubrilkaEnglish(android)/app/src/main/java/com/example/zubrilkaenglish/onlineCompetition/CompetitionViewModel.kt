package com.example.zubrilkaenglish.onlineCompetition

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.models.Profile
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.onlineCompetition.socketDto.DuelInfo
import com.example.zubrilkaenglish.onlineCompetition.socketDto.NextWord
import com.example.zubrilkaenglish.repositories.CardsRepository
import com.example.zubrilkaenglish.repositories.ProfileRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CompetitionViewModel : ViewModel() {
    private val competitionManager = CompetitionManager.instance
    private val profileRepository = ProfileRepository.instance
    private val cardsRepository = CardsRepository.instance

    val ping: MutableLiveData<Long?> = MutableLiveData(null)
    val duelInfo: MutableLiveData<DuelInfo?> = MutableLiveData(null)
    val profile: MutableLiveData<Profile?> = MutableLiveData(null)
    val startCountDown: MutableLiveData<Int?> = MutableLiveData(null)
    val nextWord: MutableLiveData<NextWord?> = MutableLiveData(null)

    init {
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
        //вернет значение startCountDown из CompetitionManager
        viewModelScope.launch{
            competitionManager.nextWord.collect{

                println("competitionManager.nextWord.collec")
                nextWord.postValue(it)
            }
        }
    }
}