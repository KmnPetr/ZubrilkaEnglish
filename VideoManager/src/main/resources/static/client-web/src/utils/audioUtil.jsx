export const playAudio = (url) => {

    // Создаем аудио элемент
    const audio = new Audio(url);

    // Переменная для хранения колбэка
    let onEndCallback = null;

    // Воспроизводим аудио
    audio.play();

    // Устанавливаем обработчик на завершение воспроизведения
    audio.onended = () => {
        // Освобождаем созданный URL
        URL.revokeObjectURL(url);

        // Если был передан колбэк, вызываем его
        if (typeof onEndCallback === 'function') {
            onEndCallback();
        }
    };

    // Возвращаем объект с методом onEnd для установки колбэка
    return {
        onEnd: (callback) => {
            if (typeof callback === 'function') {
                onEndCallback = callback;
            }
        }
    };
};