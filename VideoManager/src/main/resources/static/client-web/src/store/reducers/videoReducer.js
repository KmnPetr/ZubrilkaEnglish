/**
 * этот редюсер занимается хранением и некоторой обработкой обьекта видео, самого большого байтового обьекта
 */
const defaultState = {
    video: null
}

const SET_VIDEO = 'SET_VIDEO'


export const videoReducer = (state = defaultState, action) => {
    switch (action.type) {
        case SET_VIDEO:
            return {...state, video: action.video}
        default: return state;
    }
}

export const setVideo = (video) => ({type:SET_VIDEO, video: video})
