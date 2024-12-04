/**
 * хранит некоторые данные авторизованного пользователя в том числе и jwt токены для доступа с серверу
 */
const defaultState = {
    user: null
}

const SET_USER = 'SET_USER'

export const authReducer = (state = defaultState, action) => {
    switch (action.type) {
        case SET_USER:
            return {...state, user: action.user}
        default:
            return state;
    }
}

export const setUser = (user) => ({type: SET_USER, user: user})