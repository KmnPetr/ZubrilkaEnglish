
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
export const UPDATE_PHRASE = 'UPDATE_PHRASE'
export const UPDATE_WORDS = 'UPDATE_WORDS'
export const SET_AUDIO_UUID = 'SET_AUDIO_UUID'
export const SET_CARD_UUID = 'SET_CARD_UUID'
export const EDIT_TRANSCRIPTION = 'EDIT_TRANSCRIPTION'

// используемые типы языков
export const CN = 'cn'
export const EN = 'en'
export const RU = 'ru'

export const PHRASE = 'PHRASE'
export const WORD = 'WORD'

export const phraseReducer = (state = defaultState, action) => {

    switch (action.type) {
        case REMOVE_PHRASE:
            return {...state, phrases: state.phrases.filter((phrase) => phrase.id !== action.phraseId)}
        case EDIT_STR: {
            const newPhrase =(oldPhrase)=>{
                if(action.typeStr===PHRASE){
                    return {
                        ...oldPhrase,
                        [action.language]:{
                            ...oldPhrase[action.language],
                            str: action.newStr
                        }
                    }
                } else if(action.typeStr===WORD){
                    return {
                        ...oldPhrase,
                        words: oldPhrase.words.map((word,index)=> index===action.indexWord ? {
                            ...word,
                            [action.language]:{
                                ...word[action.language],
                                str: action.newStr
                            }
                        } : word)
                    }
                } else {
                    console.error('Invalid typeStr: '+action.typeStr)
                    return oldPhrase;
                }
            }

            return {...state, phrases: state.phrases.map((phrase) =>
                    phrase.id === action.idPhrase ?
                        newPhrase(phrase)
                        :
                        phrase
                ),
            };
        }
        case EDIT_TRANSCRIPTION: {
            const newPhrase =(oldPhrase)=>{
                if(action.typeStr===PHRASE){
                    return {
                        ...oldPhrase,
                        [action.language]:{
                            ...oldPhrase[action.language],
                            transcription: action.newTranscription
                        }
                    }
                } else if(action.typeStr===WORD){
                    return {
                        ...oldPhrase,
                        words: oldPhrase.words.map((word,index)=> index===action.indexWord ? {
                            ...word,
                            [action.language]:{
                                ...word[action.language],
                                transcription: action.newTranscription
                            }
                        } : word)
                    }
                } else {
                    console.error('Invalid typeStr: '+action.typeStr)
                    return oldPhrase;
                }
            }

            return {
                ...state,
                phrases: state.phrases.map((phrase) =>
                    phrase.id === action.idPhrase ?
                        newPhrase(phrase)
                        :
                        phrase
                ),
            };
        }
        case ADD_TO_AND: return {
            ...state,
            phrases: [...state.phrases, action.payload]};
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
        case UPDATE_PHRASE: return {
            ...state, 
            phrases: state.phrases.map((phrase) => phrase.id === action.updatedPhrase.id ? action.updatedPhrase : phrase),
        };
        case UPDATE_WORDS: return {
            ...state,
            phrases: state.phrases.map(phrase=>phrase.id === action.phraseId ? {
                ...phrase,
                words: action.newWordsList
            } : phrase)
        }
        case SET_AUDIO_UUID: 
            return {
                ...state,
                phrases: state.phrases.map(phrase=>{
                    if(phrase.id === action.phraseId){
                        if(action.typeStr === PHRASE){
                            return {
                                ...phrase,
                                [action.language]: {
                                    ...phrase[action.language],
                                    voice_uuid: action.audioUuid}
                            }
                        } else if (action.typeStr === WORD) {
                            // Обновление слова по индексу
                            const updatedWords = [...phrase.words];
                            updatedWords[action.wordIndex] = {
                                ...updatedWords[action.wordIndex],
                                [action.language]: {
                                    ...updatedWords[action.wordIndex][action.language],
                                    voice_uuid: action.audioUuid
                                }
                            };
        
                            return {
                                ...phrase,
                                words: updatedWords
                            };
                        }
                        return phrase
                    } else {
                        return phrase
                    }
                })
            }
        case SET_CARD_UUID:
            return {
                ...state,
                phrases: state.phrases.map(phrase=>{
                    if(phrase.id === action.phraseId){
                        if(action.typeStr === PHRASE){
                            return {
                                ...phrase,
                                [action.language]: {
                                    ...phrase[action.language],
                                    card_uuid: action.cardUuid}
                            }
                        } else if (action.typeStr === WORD) {
                            // Обновление слова по индексу
                            const updatedWords = [...phrase.words];
                            updatedWords[action.wordIndex] = {
                                ...updatedWords[action.wordIndex],
                                [action.language]: {
                                    ...updatedWords[action.wordIndex][action.language],
                                    card_uuid: action.cardUuid
                                }
                            };

                            return {
                                ...phrase,
                                words: updatedWords
                            };
                        }
                        return phrase
                    } else {
                        return phrase
                    }
                })
            }
        default: return state;
    }
}


export const removePhraseAction = (phraseId) => ({type: REMOVE_PHRASE, phraseId: phraseId})
export const editStrAction = ({newStr,typeStr,idPhrase,indexWord,language}) => ({type: EDIT_STR, newStr,typeStr,idPhrase,indexWord,language})
export const editTranscriptionAction=({newTranscription,typeStr,idPhrase,indexWord,language})=>({type:EDIT_TRANSCRIPTION,newTranscription,typeStr,idPhrase,indexWord,language})
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
export const updatePhrase =(updatedPhrase)=> ({type:UPDATE_PHRASE,updatedPhrase})
export const updateWordsList =(newWordsList,phraseId)=> ({type:UPDATE_WORDS,newWordsList,phraseId})
export const setAudioUuidToPhrases = (audioUuid, typeStr, phraseId, wordIndex, language) => ({type: SET_AUDIO_UUID, audioUuid, typeStr, phraseId, wordIndex, language});
export const setCardUuid = (cardUuid, typeStr, phraseId, wordIndex, language) => ({type:SET_CARD_UUID,cardUuid, typeStr, phraseId, wordIndex, language})
