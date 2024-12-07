import api from './api';

//запросит у сервера информацию по видео
export async function getVideoInfoByUuid(videoInfoUuid) {
    try {
        const response = await api.get(`/api/video-info/${videoInfoUuid}`, {
            headers: {
                'Content-Type': 'application/json'
            },
            onUploadProgress: (progressEvent) => {
                // Можно обработать прогресс загрузки, если требуется
            }
        });

        return response.data;
    } catch (error) {
        console.error('Error downloading videoInfo:', error);
        throw error;
    }
}

// Обновляет одно из полей информации по видео
export async function updateVideoInfoField(videoInfo_uuid, fieldName, newValue) {
    try {
        const response = await api.patch('/api/video-info', null, {
            params: { videoInfo_uuid, fieldName, newValue }
        });

        return response.data;
    } catch (error) {
        console.error('Error updating videoInfo:', error);
        throw error;
    }
}
