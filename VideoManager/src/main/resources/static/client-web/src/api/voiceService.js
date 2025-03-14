import api from './api';

//скачает voice
export const downloadVoiceAsMp3 = async (uuid) => {
    console.log('downloadVoiceAsMp3')
    try {
        const response = await api.get(`/api/voice/get_mp3/${uuid}`, {
            responseType: "blob" // Получаем бинарные данные
        });

        // Создаем URL для загруженного файла
        const blob = new Blob([response.data], { type: "audio/mpeg" });
        const url = URL.createObjectURL(blob);

        console.log('downloadVoiceAsMp3 ',url)

        return url;

    } catch (error) {
        console.error("Ошибка при загрузке MP3:", error);
        return null;
    }
};

//метод отправит новый voice файл на сервер
export const saveWavVoiceOnServer = async (voiceUrl,text) => {
    try {
        const response = await fetch(voiceUrl);
        const blob = await response.blob();

        const fileName = `voice.wav`;
        const file = new File([blob], fileName, { type: blob.type });

        const formData = new FormData();
        formData.append("file", file);
        formData.append("text", text);

        // Отправляем файл на сервер
        const uploadResponse = await api.post("/api/voice/save_wav_voice", formData, {
            headers: { "Content-Type": "multipart/form-data" },
        });

        return uploadResponse.data
    } catch (error) {
        console.error("Ошибка загрузки файла:", error);
    }
};