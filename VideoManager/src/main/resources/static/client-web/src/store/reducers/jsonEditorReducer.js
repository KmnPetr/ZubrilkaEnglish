/**
 * этот редюсер создан для более удобного обмена данными с модальным окном JsonEditor.jsx
 */
const defaultState = {
        isOpen:false,
        jsonObject:{},
        editableJson: '{}',
        typeObject:null //"PHRASE" or "...."
    }

const OPEN_JSON_EDITOR = 'OPEN_JSON_EDITOR'
const CLOSE_JSON_EDITOR = 'CLOSE_JSON_EDITOR';
const UPDATE_JSON_TEXT = 'SET_JSON_TEXT';

export const PHRASE = 'PHRASE'

export const jsonEditorReducer = (state = defaultState, action) => {
    switch (action.type) {
        case OPEN_JSON_EDITOR:
            return {
                ...state,
                isOpen: true,
                jsonObject: action.jsonObject,
                editableJson: JSON.stringify(action.jsonObject, null, 2),
                typeObject: action.typeObject
            }
        case CLOSE_JSON_EDITOR:
            return defaultState;//закроет окно, удалит все изменения без сохранения
        case UPDATE_JSON_TEXT:
            return { ...state, editableJson: action.payload };
        default: return state;
    }
}

export const openJsonEditor = (jsonObject,typeObject) => ({type:OPEN_JSON_EDITOR,jsonObject,typeObject})
export const closeJsonEditor = () => ({type:CLOSE_JSON_EDITOR})
export const updateJsonText = (editableJson) => ({ type: UPDATE_JSON_TEXT, payload: editableJson })