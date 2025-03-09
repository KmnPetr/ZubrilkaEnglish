import axios from 'axios';

const url ='https://zvukogram.com'
const token = 'c44fc74f205896c941f4b155a63e7d77'
const email = 'kmn.petrichenko@gmail.com'

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


export const synthesizeSpeech=async(voice,text)=> {
    
    try {
        const response = await zvukogramApi.post(
            '/index.php?r=api/text',
            {
                token: token,
                email: email,
                voice: voice,
                text: text,
                format: 'mp3',
                speed: 1.0,
                pitch: 0,
                emotion: 'good'
            },
             {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        });
        
        if (response.data.status == 1) {
            console.log("Озвучка завершена успешно:", response.data);
            console.log("Озвучка завершена успешно:", response.data.file);
            console.log("Озвучка завершена успешно:", response.data.file_cors);

            const filePath = response.data.file_cors

            const fileResponse = await fetch(filePath);
            const blob = await fileResponse.blob();
            
            // Создаем временный URL для файла
            const localUrl = URL.createObjectURL(blob);
            
            return localUrl;
        } else {
            console.error("Ошибка озвучки:", response.data.error);
        }
    } catch (error) {
        console.error("Ошибка запроса:", error.message);
    }
}