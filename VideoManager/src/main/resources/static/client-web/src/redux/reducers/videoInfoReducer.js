
//примерная структура VideoInfo обьекта на сервере, может отличаться
const defaultState = {
    uuid: null,
    cnName: null,
    enName: null,
    ruName: null,
    native_lang: null,
    used_languages: [], //использованные языки (язык оригинала + языки переводов)
    linkOriginal: null,
    translator_uuid: null,
    translator_name: null
}

const SET_VIDEO_INFO = 'SET_VIDEO_INFO'
const CLEAR = 'CLEAR'

/**
 * хранит обьект VideoInfo который связан с обьектом VideoInfo на сервере
 * в основном редактированием полей и запросами на сервер занимается компонент VideoInfo.jsx
 */
export const videoInfoReducer = (state = defaultState, action) => {
    switch (action.type) {
        case SET_VIDEO_INFO:
            return {...action.videoInfo};
        case CLEAR: return defaultState;
        default:
            return state;
    }
}

export const setVideoInfo = (videoInfo) => ({type:SET_VIDEO_INFO,videoInfo})
export const clearVideoInfo = () => ({type:CLEAR})