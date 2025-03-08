import api from './api';



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