import {downloadVideoProgress, uploadVideoProgress} from "../store/reducers/networkReducer";
import {setVideo} from "../store/reducers/videoReducer";
import api from './api';

/**
 * функция отправит фидеообьект на сервер
 * в функцию нужно передать url на этот блоб обьект
 */
export async function uploadVideo(blobUrl,videoInfo_uuid,dispatch) {
    try {
        // Получаем Blob из ссылки
        const response = await fetch(blobUrl);
        const blob = await response.blob();

        // Создаем FormData для отправки на сервер
        const formData = new FormData();
        formData.append('file', blob, 'firstVideo.mp4'); // Можно указать любое имя файла и тип

        // Отправляем FormData на сервер
        const uploadResponse = await api.post(`/api/video/upload-new?videoInfo_uuid=${videoInfo_uuid}`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
            onUploadProgress: (progressEvent) => {
                if (progressEvent.lengthComputable) {
                    const percentComplete = Math.round((progressEvent.loaded / progressEvent.total) * 100);
                    dispatch(uploadVideoProgress(`${percentComplete}%`))
                }
            }
        });

        dispatch(uploadVideoProgress(`Upload successful!`))
        console.log('Upload successful:', uploadResponse.data);
    } catch (error) {
        dispatch(uploadVideoProgress(`Upload failed!`))
        console.error('Upload failed:', error);
        throw new Error(error.response?.data?.message || 'Ошибка при отсылке видео');
    }
}


export async function downloadVideoByUUID(uuid,dispatch) {
    try {
        // Отправляем GET-запрос на сервер
        const response = await api.get(`/api/video/${uuid}`, {
            responseType: 'arraybuffer', // указываем, что ожидаем бинарный ответ
            onDownloadProgress: (progressEvent) => {
                if (progressEvent.total) {
                    // Вычисляем процент загруженного видео
                    const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
                    dispatch(downloadVideoProgress(`${percentCompleted}%`))
                }
            },
        });

        // Обрабатываем ответ
        const videoBlob = new Blob([response.data], { type: 'video/mp4' });
        const videoUrl = URL.createObjectURL(videoBlob);

        dispatch(setVideo({
            videoUrl: videoUrl,
            uuid: response.headers['UUID'],
            fileName: response.headers['X-Filename'],
        }))

        dispatch(downloadVideoProgress('Download successful!'))
    } catch (error) {
        dispatch(downloadVideoProgress('Error download video!'))
        console.error('Error fetching video:', error);

        if (error.response && error.response.status === 404) {
            dispatch(downloadVideoProgress('Video not found!'))
            console.error('Video not found');
        } else {
            console.error('Unknown error occurred');
        }
        return null;
    }
}

//запросит у сервера список видео
export async function downloadListVideo() {
    try {
        const response = await api.get('/api/video/list-video', {
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