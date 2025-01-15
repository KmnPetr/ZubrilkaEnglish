import { REMOVE_PHRASE, EDIT_STR, ADD_TO_AND, ADD_NEW, EDIT_TIME } from './reducers/phraseReduser';
import {sendUpdateToServer} from "../services/phrasesService"


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
    ];

    if (actionsToSync.includes(action.type)) {
        sendUpdateToServer(state.phraseReducer.phrases, state.translationReducer.translation.videoInfoUuid); // Вызываем функцию
    }

    return result; // Возвращаем результат для поддержки цепочки middleware
};