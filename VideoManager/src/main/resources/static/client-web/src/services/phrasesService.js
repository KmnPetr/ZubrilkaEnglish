import api from './api';

export async function getTranslation(translation_uuid) {
    try {
        const response = await api.get(`/api/translation/${translation_uuid}`, {
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