import { data } from 'react-router-dom';
import api from './api';

export async function getTranslation(videoInfo_uuid) {
    try {
        const response = await api.get(`/api/translation/${videoInfo_uuid}`, {
            headers: {
                'Content-Type': 'application/json'
            },
            onUploadProgress: (progressEvent) => {
                // Можно обработать прогресс загрузки, если требуется
            }
        });

        return response.data;
    } catch (error) {
        console.error('Error downloading video list:', error);
        throw error;
    }
}

// Sending an updated list of phrases to the server
export async function sendUpdateToServer(phrases,translation_uuid) {
    console.log("Отправка данных на сервер:", phrases);

    try {
        const response = await api.put(`/api/translation/${translation_uuid}/phrases`, phrases,
            {
                headers: {
                    'Content-Type': 'application/json',
                },
                onUploadProgress: (progressEvent) => {
                    // Можно обработать прогресс загрузки, если требуется
                },
            }
        );

        return response.data;
    } catch (error) {
        console.error('Error sending phrases:', error);
        throw error;
    }
};