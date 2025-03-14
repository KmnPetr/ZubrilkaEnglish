import api from './api';

// Функция для выполнения логина
export const sendIconToServer = async (screenshot,videoInfo_uuid) => {

    try {
      // Преобразование URL изображения в Blob
      const response = await fetch(screenshot);
      const blob = await response.blob();

      // Создание FormData для отправки файла
      const formData = new FormData();
      formData.append('file', blob, 'screenshot.png'); // 'file' - имя параметра для сервера

      // Отправка файла на сервер с помощью axios
      const serverResponse = await api.post(`/api/icon/upload/${videoInfo_uuid}`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

    } catch (error) {
      console.error('Ошибка при отправке изображения:', error);
    }

};

export const downloadIcon = async (videoInfo_uuid) => {
  try {
      const response = await api.get(`/api/icon/download/${videoInfo_uuid}`, { responseType: "blob" });
      const imageUrl = URL.createObjectURL(response.data);
      return imageUrl;
  } catch (error) {
      console.error("Ошибка загрузки изображения", error);
  }
};