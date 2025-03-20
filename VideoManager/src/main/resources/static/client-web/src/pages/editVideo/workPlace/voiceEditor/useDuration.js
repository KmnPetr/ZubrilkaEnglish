import { useState, useEffect } from "react";

//посчитает длительность аудио особым образом, потому что в обычных метаданных записи аудио в значении duration стоит infinity
export function useDuration(audioURL) {
  const [duration,setDuration] = useState(0)

  //подсчитываем длительность аудио
  useEffect(()=>{
    const audioContext = new (window.AudioContext || window.webkitAudioContext)();
    const xhr = new XMLHttpRequest();
    xhr.open('GET', audioURL, true);
    xhr.responseType = 'arraybuffer';
  
    xhr.onload = function() {
      audioContext.decodeAudioData(xhr.response, function(buffer) {
        // Получаем длительность аудиофайла в секундах
        setDuration(buffer.duration)
      }, function(error) {
        console.error('Ошибка при декодировании аудио:', error);
      });
    };
  
    xhr.onerror = function() {
      console.error('Ошибка при загрузке файла.');
    };
  
    xhr.send();
},[audioURL])

  return {duration};
};
