import {setUser} from "../redux/reducers/authReducer";
import api from './api';

// Функция для выполнения логина
export const login = async (username, password,dispatch) => {
    try {
        const response = await api.post('/api/auth/login', {
            username,
            password
        });
        dispatch(setUser(response.data))
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Login failed. Please try again.');
    }
};

// Функция для выполнения логина
// Истользуется точка логина выдающая jwt-токен в виде куки
export const login_v2 = async (username, password,dispatch) => {


    const formData = new URLSearchParams();
    formData.append('username', username);
    formData.append('password', password);
    

    try {
        const response = await api.post('/api/login', formData, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
        });

        dispatch(setUser(response.data))
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Login failed. Please try again.');
    }
};