import { useRef, useState } from "react";
import toWav from "audiobuffer-to-wav";

export function useAudioPlayer(audioURL,startTime,endTime) {
  const audioCutRef = useRef(null); // Хранит обрезанный объект Audio
  const [isPlaying, setIsPlaying] = useState(false); // Отслеживает состояние воспроизведения


  const play = () => {

    if(audioURL && endTime){
      trimAudio2(audioURL,startTime, endTime).then((trimmedAudioURL) => {

        
      // Очищаем предыдущий объект audioCutRef, если он существовал
      if (audioCutRef.current) {
        audioCutRef.current.pause();
        audioCutRef.current = null;
      }

      audioCutRef.current = new Audio(trimmedAudioURL);
      audioCutRef.current.onended = () => setIsPlaying(false);// Сбрасываем состояние при завершении

      if (audioCutRef.current) {
        audioCutRef.current.play().then(() => setIsPlaying(true)).catch(console.error);
      }
      });
    }
  };

  const pause = () => {
    if (audioCutRef.current) {
      audioCutRef.current.pause();
      setIsPlaying(false);
    }
  };

  const getTrimmedAudio = async ()=>{
    return await trimAudio2(audioURL, startTime, endTime)
  }


  return { play, pause, isPlaying, getTrimmedAudio };
}

/**
 * обрежет wav файл с начала и сконца
 * вернет ссылку на новый файл
 */
const trimAudio2 = async (audioURL, startTime, endTime) => {
  const response = await fetch(audioURL);
  const audioData = await response.arrayBuffer();

  // Раскодируем аудиофайл в AudioBuffer
  const audioContext = new AudioContext();
  const buffer = await audioContext.decodeAudioData(audioData);

  // Параметры обрезки
  const sampleRate = buffer.sampleRate;
  const startSample = Math.floor(startTime * sampleRate); // Начало
  const endSample = Math.min(Math.floor(endTime * sampleRate), buffer.length); // Конец
  const trimmedLength = endSample - startSample;

  // Создаём новый AudioBuffer для обрезанного аудио
  const trimmedBuffer = audioContext.createBuffer(
    buffer.numberOfChannels,
    trimmedLength,
    sampleRate
  );

  // Копируем данные из исходного AudioBuffer, начиная с startSample
  for (let channel = 0; channel < buffer.numberOfChannels; channel++) {
    const channelData = buffer.getChannelData(channel).slice(startSample, endSample);
    trimmedBuffer.copyToChannel(channelData, channel);
  }

  // Преобразуем обрезанный AudioBuffer в WAV
  const wav = toWav(trimmedBuffer);
  const blob = new Blob([wav], { type: "audio/wav" });
  const url = URL.createObjectURL(blob);

  return url; // Возвращаем ссылку на обрезанный WAV
};