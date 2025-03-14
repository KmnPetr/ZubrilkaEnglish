import {
    REMOVE_PHRASE,
    EDIT_STR,
    ADD_TO_AND,
    ADD_NEW,
    EDIT_TIME,
    UPDATE_PHRASE,
    UPDATE_WORDS,
    SET_AUDIO_UUID
} from './reducers/phraseReduser';
import {sendUpdateToServer} from "../api/phrasesService"


export const phrasesMiddleware = (store) => (next) => (action) => {
    const result = next(action); // Передаём действие дальше в reducer
    const state = store.getState(); // Получаем текущее состояние

    // Список действий, которые должны триггерить `sendUpdateToServer`
    const actionsToSync = [
        REMOVE_PHRASE,
        EDIT_STR,
        ADD_TO_AND,
        ADD_NEW,
        EDIT_TIME,
        UPDATE_PHRASE,
        UPDATE_WORDS,
        SET_AUDIO_UUID
    ];

    if (actionsToSync.includes(action.type)) {
        sendUpdateToServer(state.phraseReducer.phrases, state.translationReducer.translation.videoInfoUuid); // Вызываем функцию
    }

    return result; // Возвращаем результат для поддержки цепочки middleware
};