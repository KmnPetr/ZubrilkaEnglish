import axios from 'axios';

// Создание экземпляра клиента Axios с базовыми настройками
const api = axios.create({
    baseURL: window.location.origin,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Обработка ответов и ошибок
api.interceptors.response.use(
    response => response,
    error => {
        // Обработка ошибок (например, если токен истек, перенаправить на страницу входа)
        return Promise.reject(error);
    }
);

export default api;