
const defaultState = {
    network: {
        uploadVideoProgress: null,
        downloadVideoProgress: null
    }
}

const UPLOAD_VIDEO_PR = 'UPLOAD_VIDEO_PR' //прогресс отправки файла видео на сервер
const DOWNLOAD_VIDEO_PR = 'DOWNLOAD_VIDEO_PR' //прогресс получения

export const networkReducer = (state = defaultState, action) => {
    switch (action.type) {
        case UPLOAD_VIDEO_PR:
            return {...state, network: {...state.network, uploadVideoProgress: action.value}}
        case DOWNLOAD_VIDEO_PR:
            return {...state, network: {...state.network, downloadVideoProgress: action.value}}
        default:
            return state;
    }
}

export const uploadVideoProgress = (value) => ({type: UPLOAD_VIDEO_PR, value})
export const downloadVideoProgress = (value) => ({type: DOWNLOAD_VIDEO_PR, value})