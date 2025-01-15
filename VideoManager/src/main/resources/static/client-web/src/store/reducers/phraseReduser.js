
const defaultState = {
    phrases: null
}

export const REMOVE_PHRASE = 'REMOVE_PHRASE'
export const EDIT_STR = 'EDIT_STR'
export const ADD_TO_AND = 'ADD_TO_AND'
export const ADD_NEW = 'ADD_NEW'
export const EDIT_TIME = 'EDIT_TIME'
export const SET_LIST_PHRASES = 'SET_LIST_PHRASES'
export const CLEAR = 'CLEAR'

// используемые типы языков
export const CN = 'cn'
export const EN = 'en'
export const RU = 'ru'

export const phraseReducer = (state = defaultState, action) => {
    switch (action.type) {
        case REMOVE_PHRASE:
            return {...state, phrases: state.phrases.filter((phrase) => phrase.id !== action.phraseId)}
        case EDIT_STR:

            // Определение поля для обновления в зависимости от языка
            const getFieldName = (lang) => {
                switch (lang) {
                    case CN: return 'cnStr';
                    case EN: return 'enStr';
                    case RU: return 'ruStr';
                    default: return null;
                }
            };

            const fieldName = getFieldName(action.language);

            return {...state, phrases: state.phrases.map((phrase) => phrase.id === action.phraseId ?
                    {...phrase, [fieldName]: action.newValue }
                    :
                    phrase
                ),
            };
        case ADD_TO_AND: return {...state, phrases: [...state.phrases, action.payload]};
        case ADD_NEW:
            // Добавляем новый элемент и сортируем массив по полю startTime
            const updatedPhrases = [...state.phrases, action.payload].sort((a, b) => a.startTime - b.startTime);

            return {
                ...state,
                phrases: updatedPhrases // Обновляем массив в состоянии
            };
        case EDIT_TIME: return {...state, phrases: state.phrases.map((phrase) => phrase.id === action.idPhrase ?
                {...phrase, [action.fieldName]: action.newValue }
                :
                phrase),};
        case SET_LIST_PHRASES: return {...state, phrases: action.phrases}
        case CLEAR: return defaultState;
        default: return state;
    }
}


export const removePhraseAction = (phraseId) => ({type: REMOVE_PHRASE, phraseId: phraseId})
export const editStrAction = (phraseId,language,newValue) => ({type: EDIT_STR, phraseId:phraseId,language:language,newValue:newValue})
/**
 * добавит фразу в конец списка
 */
export const addNewPhraseToEnd = () => {
    return (dispatch, getState) => {
        const phrases = getState().phraseReducer.phrases;

        const lastElement = Array.isArray(phrases) && phrases.length > 0 
        ? phrases[phrases.length - 1] 
        : null;

        // Создаем новую фразу
        const newPhrase = {
            id: Date.now(),
            cnStr: '',
            enStr: '',
            ruStr: '',
            startTime: 0,
            endTime: 0
        };

        if (lastElement===null){
            newPhrase.startTime = 0;
            newPhrase.endTime = newPhrase.startTime+1000
        } else {
            newPhrase.startTime = lastElement.startTime+1000
            newPhrase.endTime = newPhrase.startTime+1000
        }

        dispatch({type: ADD_TO_AND,payload: newPhrase });
    };
};
/**
 * добавит новую фразу перед или после существующей
 * метод имеет некоторую сложность расчета времени старта и конца фразы
 */
export const addNewPhrase = (id, position) => {
    return (dispatch, getState) => {

        // Создаем новую фразу
        const newPhrase = {
            id: Date.now(),
            cnStr: '',
            enStr: '',
            ruStr: '',
            startTime: 0,
            endTime: 0
        };
        const phrases = getState().phraseReducer.phrases;
        const currentElIndex = phrases.findIndex(el => el.id === id);
        const currentEl = currentElIndex !== -1 ? phrases[currentElIndex] : null;
        const previousEl = currentElIndex > 0 && currentElIndex - 1 >= 0 ? phrases[currentElIndex - 1] : null;
        const nextEl = currentElIndex !== -1 && currentElIndex + 1 < phrases.length ? phrases[currentElIndex + 1] : null;

        if (position==='before'){ //устанавливаем временной промежуток до текущего элемента
            if (previousEl!==null&&previousEl.endTime<currentEl.startTime){
                // примерно поделим временной промежуток до предыдущего элемента на три доли и в среднюю долю поместим новый элемент
                const p = Math.floor((currentEl.startTime - previousEl.endTime)/3);
                if (p<10000){
                    newPhrase.startTime = previousEl.endTime + p
                    newPhrase.endTime = previousEl.endTime + p*2
                } else if (p>=10000){
                    // промежуток то предыдущего элемента оказался слишком большим, установим новый элемент просто на небольшое время назад
                    newPhrase.startTime = currentEl.startTime - 4000
                    newPhrase.endTime = currentEl.startTime - 2000
                }
            }else if (currentEl.startTime>5000){
                //что-то не в порядке с предыдущим элементом, установим новый элемент просто на небольшое время назад
                newPhrase.startTime = currentEl.startTime - 4000
                newPhrase.endTime = currentEl.startTime - 2000
            } else { /*оставим начальное и конечное время по нулям*/ }


        }else if(position==='after'){ //устанавливаем временной промежуток после элемента
            if (nextEl!==null && nextEl.startTime>currentEl.endTime){
                // со следующим элементом все впорядке
                // примерно поделим временной промежуток до следующего элемента на три доли и в среднюю долю поместим новый элемент
                const p = Math.floor((nextEl.startTime-currentEl.endTime)/3);
                if (p<10000){
                    newPhrase.startTime = currentEl.endTime + p
                    newPhrase.endTime =  currentEl.endTime + p*2
                } else if (p>=10000){
                    // промежуток то следующего элемента оказался слишком большим, установим новый элемент просто на небольшое время вперед
                    newPhrase.startTime = currentEl.endTime + 2000
                    newPhrase.endTime = currentEl.endTime + 4000
                }
            }else{
                //что-то не так со следующим элементом, просто установим новый элемент на небольшое время вперед
                newPhrase.startTime = currentEl.endTime + 2000
                newPhrase.endTime = currentEl.endTime + 4000
            }
        } else { /*оставим начальное и конечное время по нулям*/ }

        dispatch({type: ADD_NEW, payload: newPhrase})
    }
};
export const editTime = (idPhrase, newValue, fieldName) => {
    return {type: EDIT_TIME, idPhrase: idPhrase, newValue: newValue, fieldName: fieldName}
}
export const setListPhrases = (phrases) => ({type: SET_LIST_PHRASES, phrases: phrases})

export const clearPhrases = () => ({type:CLEAR})