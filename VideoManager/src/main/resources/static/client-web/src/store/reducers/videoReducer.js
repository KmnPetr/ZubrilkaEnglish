/**
 * этот редюсер занимается хранением и некоторой обработкой обьекта видео, самого большого байтового обьекта
 */
const defaultState = {
    video: {
        videoUrl: null,
        isExist: true //в случае первой прогрузки если videoServise установит false, то скажем что видео не установлено а пока верим что оно имеется
    }
}

const SET_VIDEO = 'SET_VIDEO'
const SET_IS_EXIST = 'SET_IS_EXIST'


export const videoReducer = (state = defaultState, action) => {
    switch (action.type) {
        case SET_VIDEO:
            return {...state, video: action.video}
        case SET_IS_EXIST:
            return {...state, video: {...state.video, isExist: action.isExist}}
        default: return state;
    }
}

export const setVideo = (video) => ({type:SET_VIDEO, video: video})
export const clearVideo =()=> ({type:SET_VIDEO, video: defaultState.video})
export const setIsVideoExist =(isExist)=> ({type:SET_IS_EXIST, isExist: isExist})
