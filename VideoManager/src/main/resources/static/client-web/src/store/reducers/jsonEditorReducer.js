/**
 * этот редюсер создан для более удобного обмена данными с модальным окном JsonEditor.jsx
 */
const defaultState = {
        isOpen:false,
        code: `{"name": "John","age": 30,"city": "New York"}`
    }

const OPEN_JSON_EDITOR = 'OPEN_JSON_EDITOR'
const CLOSE_JSON_EDITOR = 'CLOSE_JSON_EDITOR';
const SET_JSON_CODE = 'SET_JSON_CODE';


export const jsonEditorReducer = (state = defaultState, action) => {
    switch (action.type) {
        case OPEN_JSON_EDITOR:
            return {...state, isOpen: true}
        case CLOSE_JSON_EDITOR:
            return defaultState;//закроет окно, удалит все изменения без сохранения
        case SET_JSON_CODE:
            return { ...state, code: action.payload };
        default: return state;
    }
}

export const openJsonEditor = () => ({type:OPEN_JSON_EDITOR})
export const closeJsonEditor = () => ({type:CLOSE_JSON_EDITOR})
export const setJsonCode = (code) => ({ type: SET_JSON_CODE, payload: code })