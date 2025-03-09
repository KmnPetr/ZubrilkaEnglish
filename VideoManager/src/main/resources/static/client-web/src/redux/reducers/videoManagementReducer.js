

const defaultState = {
    videoManagement: {
        phraseInterval: { // интервал фразы
            startTime: 0,
            endTime: 0,
            toPlay: false, //если true, то видео компонент проиграет этот интервал затем установит false
            toPause: false, //исключит некоторые баги чтобы два раза на паузу не ставилось
            idPhrase: null
        },
        videoPath: null
    }
}

const SET_INTERVAL = 'SET_INTERVAL' //установит новое значение для воспроизведения интервала видео
const SET_TO_PLAY_FALSE = 'SET_TO_PLAY_FALSE'
const SET_TO_PAUSE_FALSE = 'SET_TO_PAUSE_FALSE'
const SAVE_VIDEO_PATH = 'SAVE_VIDEO_PATH'

export const videoManagementReducer = (state = defaultState, action) => {
    switch (action.type) {
        case SET_INTERVAL:
            return {...state, videoManagement: {
                    ...state.videoManagement, phraseInterval: {
                        ...state.videoManagement.phraseInterval,
                        startTime: action.phrase.startTime,
                        endTime: action.phrase.endTime,
                        idPhrase: action.phrase.id,
                        toPlay: true,
                        toPause: true
                    }
                }
            };
        case SET_TO_PLAY_FALSE:
            return {...state, videoManagement: {
                    ...state.videoManagement, phraseInterval: {
                        ...state.videoManagement.phraseInterval, toPlay: false}}};
        case SET_TO_PAUSE_FALSE:
            return {...state, videoManagement: {
                    ...state.videoManagement, phraseInterval: {
                        ...state.videoManagement.phraseInterval, toPause: false}}};
        case SAVE_VIDEO_PATH:
            return {...state, videoManagement: {...state.videoManagement, videoPath: action.videoPath}}
        default:
            return state;
    }
}

export const setIntervalAction = (phrase) => ({
    type: SET_INTERVAL,
    phrase: phrase
});
export const setToPlayFalse = () => ({type: 'SET_TO_PLAY_FALSE'})
export const setToPauseFalse = () => ({type: 'SET_TO_PAUSE_FALSE'})
export const saveVideoPath = (videoURL) => ({type:SAVE_VIDEO_PATH,videoPath:videoURL})