/**
 * этот редюсер создан для более удобного обмена данными с модальным окном VoiceEditor.jsx
 */
const defaultState = {
    isOpen:false,
    idPhrase: null,
    indexWord: null, //если voice редактируется у word
    typeStr:null, //"PHRASE" or "WORD"
    str: null,
    language:null
}

const OPEN_VOICE_EDITOR = 'OPEN_VOICE_EDITOR'
const CLOSE_VOICE_EDITOR = 'CLOSE_VOICE_EDITOR'

export const PHRASE = 'PHRASE'
export const WORD = 'WORD'

export const voiceEditorReducer = (state = defaultState, action) => {
    switch (action.type) {
        case OPEN_VOICE_EDITOR:
            return {
                ...defaultState,
                isOpen: true,
                idPhrase: action.idPhrase,
                indexWord: action.indexWord,
                typeStr: action.typeStr,
                str: action.str,
                language: action.language
            }
        case CLOSE_VOICE_EDITOR:
            return defaultState;//закроет окно, удалит все изменения без сохранения
        default: return state;
    }
}

export const openVoiceEditor = ({idPhrase,indexWord,str,language,typeStr}) => ({type:OPEN_VOICE_EDITOR,idPhrase,indexWord,str,language,typeStr})
export const closeVoiceEditor = () => ({type:CLOSE_VOICE_EDITOR})