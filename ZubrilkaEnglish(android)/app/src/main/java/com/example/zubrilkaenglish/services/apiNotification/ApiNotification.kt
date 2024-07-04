package com.example.zubrilkaenglish.services.apiNotification

import android.util.Log
import com.example.zubrilkaenglish.events.CardEvent
import com.example.zubrilkaenglish.events.CrEvEnum
import com.example.zubrilkaenglish.events.NfEvEnum
import com.example.zubrilkaenglish.events.NotificationEvent
import com.example.zubrilkaenglish.events.iEvent
import com.example.zubrilkaenglish.screens.MainActivity
import android.view.View
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.zubrilkaenglish.utils.LOG
import com.google.android.material.snackbar.Snackbar

/**
 * класс отвечает за показ различных уведомлений пользователю
 * в частности показ Toast сообщения
 * для показа
 */
class ApiNotification private constructor() {
    companion object{
        val instance: ApiNotification by lazy { ApiNotification() }
    }


    /**
     * MainActivity отлавливает все евенты из EventBus и передает их сюда
     */
    fun <T : Enum<T>, E : iEvent<T>> handleEvent(event: E, activity: MainActivity) {
        Log.d(LOG,"Event: ${event.typeEvent.name}") //покажет все ивенты в приложении от EventBus

        when(event.typeEvent){
            CrEvEnum.CARD_CHANGED -> cardChanged(event as CardEvent,activity)
            NfEvEnum.LIMIT_ACTIVE_WORDS -> limitActiveWords(event as NotificationEvent,activity)
            NfEvEnum.GO_TO_CATALOG -> activity.goToCatalog()
            NfEvEnum.GO_TO_MEMOS -> activity.goToMemos()
            NfEvEnum.GO_TO_UPSTACK -> activity.popBackStack()
        }
    }


    //покажет уведомление что наступил лимит активных карточек
    private fun limitActiveWords(event: NotificationEvent, activity: MainActivity) {
        PopupLimitActiveCards(event,activity).show()
    }

    /**
     *  выдает уведомление на изменения в карточках
     */
    private fun cardChanged(event: CardEvent, activity: MainActivity) {

        val notif:String = event.properties["notif"].toString()
        when(notif){
            "deleteCard" -> {
                Snackbar.make(rootView(activity), "\"${event.wordCard.word.foreignWord}\" карточка удалена", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            }
            "learnCard" -> {
                Snackbar.make(rootView(activity), "\"${event.wordCard.word.foreignWord}\" карточка выучена", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            }
            "resetProgress" -> {
                Snackbar.make(rootView(activity), "\"${event.wordCard.word.foreignWord}\" прогресс сброшен", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            }
            "addToWordTrain" -> {
                Snackbar.make(rootView(activity), "\"${event.wordCard.word.foreignWord}\" добавлена для изучения", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            }
        }
    }

    private fun rootView(activity: MainActivity): View {
        return activity.findViewById(android.R.id.content)
    }

}
 enum class NotifProp(val pair: Map<String, String>) {
     deleteCard(mapOf("notif" to "deleteCard")),
     learnCard(mapOf("notif" to "learnCard")),
     resetProgress(mapOf("notif" to "resetProgress")),
     addToWordTrain(mapOf("notif" to "addToWordTrain"))

 }