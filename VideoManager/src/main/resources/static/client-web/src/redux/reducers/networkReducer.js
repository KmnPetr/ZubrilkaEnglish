
const defaultState = {
    network: {
        uploadVideoProgress: null,
        downloadVideoProgress: null,
        isIconChanged: 0, //любое изменение этого поля провоцирует перезагрузку установленной иконки
    }
}

const UPLOAD_VIDEO_PR = 'UPLOAD_VIDEO_PR' //прогресс отправки файла видео на сервер
const DOWNLOAD_VIDEO_PR = 'DOWNLOAD_VIDEO_PR' //прогресс получения
const CLEAR_VIDEO_PROGRESS = 'CLEAR_VIDEO_PROGRESS'
const ICON_CHANGED = 'ICON_CHANGED'

export const networkReducer = (state = defaultState, action) => {
    switch (action.type) {
        case UPLOAD_VIDEO_PR:
            return {...state, network: {...state.network, uploadVideoProgress: action.value}}
        case DOWNLOAD_VIDEO_PR:
            return {...state, network: {...state.network, downloadVideoProgress: action.value}}
        case CLEAR_VIDEO_PROGRESS:
            return {...state, network:{
                ...state.network,
                uploadVideoProgress: null,
                downloadVideoProgress: null }}
                case ICON_CHANGED: return {...state, network: {...state.network, isIconChanged: state.network.isIconChanged + 1}};
        default:
            return state;
    }
}

export const uploadVideoProgress = (value) => ({type: UPLOAD_VIDEO_PR, value})
export const downloadVideoProgress = (value) => ({type: DOWNLOAD_VIDEO_PR, value})
export const clearVideoProgress =()=> ({type: CLEAR_VIDEO_PROGRESS})
export const iconWasChanged =()=> ({type: ICON_CHANGED})