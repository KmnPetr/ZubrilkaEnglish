/**
 * этот редюсер создан для более удобного обмена данными с модальным окном CardEditor.jsx
 * почти копия voiceEditorReducer.js
 */
const defaultState = {
    isOpen: false,
    idPhrase: null,
    indexWord: null, //если card редактируется у word
    typeStr: null, //"PHRASE" or "WORD"
    str: null,
    language: null
};

const OPEN_CARD_EDITOR = 'OPEN_CARD_EDITOR'
const CLOSE_CARD_EDITOR = 'CLOSE_CARD_EDITOR'

export const PHRASE = 'PHRASE'
export const WORD = 'WORD'

export const cardEditorReducer = (state = defaultState, action) => {
    switch (action.type) {
        case OPEN_CARD_EDITOR:
            return {
                ...defaultState,
                isOpen: true,
                idPhrase: action.idPhrase,
                indexWord: action.indexWord,
                typeStr: action.typeStr,
                str: action.str,
                language: action.language
            }
        case CLOSE_CARD_EDITOR:
            return defaultState;//закроет окно, удалит все изменения без сохранения
        default: return state;
    }
}

export const openCardEditor = ({idPhrase,indexWord,str,language,typeStr}) => ({type:OPEN_CARD_EDITOR,idPhrase,indexWord,str,language,typeStr})
export const closeCardEditor = () => ({type:CLOSE_CARD_EDITOR})