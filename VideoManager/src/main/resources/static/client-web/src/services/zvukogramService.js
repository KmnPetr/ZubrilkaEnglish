import axios from 'axios';

const url ='https://zvukogram.com'
// Создание экземпляра клиента Axios с базовыми настройками
const zvukogramApi = axios.create({
    baseURL: url,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Обработка ответов и ошибок
zvukogramApi.interceptors.response.use(
    response => response,
    error => {
        // Обработка ошибок (например, если токен истек, перенаправить на страницу входа)
        return Promise.reject(error);
    }
);


export const getListVoices = async () => {
    try {
        const response = await zvukogramApi.get(`/index.php?r=api/voices`)
        return response.data
    } catch (error) {
        console.error("Ошибка загрузки Голосов Звукограмм", error);
    }
  };