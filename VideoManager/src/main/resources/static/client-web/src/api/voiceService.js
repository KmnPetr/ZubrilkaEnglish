import api from './api';

//скачает voice
export const downloadVoiceAsMp3 = async (uuid) => {
    try {
        const response = await api.get(`/api/voice/get_mp3/${uuid}`, {
            responseType: "blob" // Получаем бинарные данные
        });

        // Создаем URL для загруженного файла
        const blob = new Blob([response.data], { type: "audio/mpeg" });
        const url = URL.createObjectURL(blob);

        return url;

    } catch (error) {
        console.error("Ошибка при загрузке MP3:", error);
        return null;
    }
};

//метод отправит новый voice файл на сервер
export const saveWavVoiceOnServer = async (voiceUrl,text,{voice,sex}) => {
    try {
        const response = await fetch(voiceUrl);
        const blob = await response.blob();

        const fileName = `voice.wav`;
        const file = new File([blob], fileName, { type: blob.type });

        const formData = new FormData();
        formData.append("file", file);
        formData.append("text", text);
        formData.append("voice", voice);
        formData.append("sex", sex);

        // Отправляем файл на сервер
        const uploadResponse = await api.post("/api/voice/save_wav_voice", formData, {
            headers: { "Content-Type": "multipart/form-data" },
        });

        return uploadResponse.data
    } catch (error) {
        console.error("Ошибка загрузки файла:", error);
    }
};

//запросит с сервера список voice ранее озвученных по тексту похожих
export const getListSimilarVoices = async ({ text, translation_uuid }) => {
    try {
        const response = await api.post(`/api/voice/list_similar_voices`, {
            text,
            translation_uuid
        }, {
            headers: { 'Content-Type': 'application/json' }
        });
        return response.data;
    } catch (e) {
        return Promise.reject(e);
    }
};