/**
 * хранит некоторые данные авторизованного пользователя в том числе и jwt токены для доступа с серверу
 */
const defaultState = {
    user: null
}

const SET_USER = 'SET_USER'
const UPDATE_RATING_VOICES = 'UPDATE_RATING_VOICES'

export const authReducer = (state = defaultState, action) => {
    switch (action.type) {
        case SET_USER:
            return {...state, user: action.user}
        case UPDATE_RATING_VOICES:
            return {...state, user: {...state.user, rating_voices: action.ratingVoices}}
        default:
            return state;
    }
}

export const setUser = (user) => ({type: SET_USER, user: user})
export const updateRatingVoicesRedux = (ratingVoices)=>({type: UPDATE_RATING_VOICES, ratingVoices})