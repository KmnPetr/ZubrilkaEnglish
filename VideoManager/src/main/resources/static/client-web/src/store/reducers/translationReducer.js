

const defaultState = {
    translation: null
}

const SET_TRANSLATION = 'SET_TRANSLATION'
const CLEAR = 'CLEAR'

/**
 * this reduser stores information on video translation,
 * but the list of translated phrases is stored in a separate phraseReduser for easy manipulation of the list.
 * in this reduser, the phrase list field should always be set as null to avoid confusion of logic
 */
export const translationReducer = (state = defaultState, action) => {
    switch (action.type) {
        case SET_TRANSLATION:
            return {
                ...state,
                translation: {
                    ...action.translation,
                    phrases: null // always force phrases to be null
                }
            };
        case CLEAR: return defaultState;
        default:
            return state;
    }
}

export const setTranslation = (translation) => ({type:SET_TRANSLATION,translation:translation})
export const clearTranslation = () => ({type:CLEAR})
