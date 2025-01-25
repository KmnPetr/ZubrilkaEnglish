import { useEffect, useRef, useState } from "react";

export function useAudioPlayer(audioURL,startTime,endTime) {
  const audioRef = useRef(null); // Хранит объект Audio
  const [isPlaying, setIsPlaying] = useState(false); // Отслеживает состояние воспроизведения
  const [currentTime, setCurrentTime] = useState(0); // Текущее время аудио

  // Обновляем объект Audio при изменении audioURL
  useEffect(() => {
    if (audioURL) {
      // Очищаем предыдущий объект Audio, если он существовал
      if (audioRef.current) {
        audioRef.current.pause();
        audioRef.current = null;
      }

      audioRef.current = new Audio(audioURL);
      audioRef.current.onended = () => setIsPlaying(false); // Сбрасываем состояние при завершении

      // Обновляем текущее время при воспроизведении
      audioRef.current.ontimeupdate = () => {
        setCurrentTime(audioRef.current.currentTime || 0);
      };
    }
  }, [audioURL]);

  const play = () => {
    if (audioRef.current) {
      audioRef.current.play().then(() => setIsPlaying(true)).catch(console.error);
    }
  };

  const pause = () => {
    if (audioRef.current) {
      audioRef.current.pause();
      setIsPlaying(false);
    }
  };

  const reset = () => {
    if (audioRef.current) {
      audioRef.current.pause();
      audioRef.current.currentTime = 0;
      setIsPlaying(false);
    }
  };

  return { play, pause, reset, isPlaying, currentTime };
}
