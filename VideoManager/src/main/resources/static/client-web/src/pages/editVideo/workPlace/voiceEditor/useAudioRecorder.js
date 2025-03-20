import { useState, useRef, useEffect } from "react";

const useAudioRecorder = () => {
  const [isRecording, setIsRecording] = useState(false);
  const [audioURL, setAudioURL] = useState(null);
  const [isReady, setIsReady] = useState(false);
  const [permissionGranted, setPermissionGranted] = useState(false);

  const mediaRecorderRef = useRef(null);
  const audioChunks = useRef([]);

  // Проверяет наличие разрешения на микрофон
  const checkPermission = async () => {
    try {
      const permissionStatus = await navigator.permissions.query({ name: "microphone" });
      setPermissionGranted(permissionStatus.state === "granted");

      // Подписка на изменения разрешения (опционально)
      permissionStatus.onchange = () => {
        setPermissionGranted(permissionStatus.state === "granted");
      };

      return permissionStatus.state === "granted";
    } catch (error) {
      console.error("Error checking microphone permission:", error);
      return false;
    }
  };

  const initRecorder = async () => {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
      mediaRecorderRef.current = new MediaRecorder(stream);

      mediaRecorderRef.current.ondataavailable = (event) => {
        if (event.data.size > 0) {
          audioChunks.current.push(event.data);
        }
      };

      mediaRecorderRef.current.onstop = () => {
        const audioBlob = new Blob(audioChunks.current, { type: "audio/wav" });
        const url = URL.createObjectURL(audioBlob);
        setAudioURL(url);
        audioChunks.current = [];

      };

      setIsReady(true); // Устанавливаем готовность
      setPermissionGranted(true); // Обновляем состояние разрешения
    } catch (error) {
      console.error("Error initializing audio recorder:", error);
    }
  };

  const startRecording = async () => {
    if (!permissionGranted) {
      await initRecorder(); // Инициализируем рекордер, пытаемся получить разрешение
    } else if (!isReady && !isRecording) {
      await initRecorder(); // Инициализируем запись, если это первый вызов
      mediaRecorderRef.current.start();
      setIsRecording(true);
    } else if (!isRecording) {
      mediaRecorderRef.current.start();
      setIsRecording(true);
    }
  };

  const stopRecording = () => {
    if (mediaRecorderRef.current && isRecording) {
      mediaRecorderRef.current.stop();
      setIsRecording(false);
    }
  };


  // Проверяем разрешение на микрофон при инициализации хука
  useEffect(() => {
    const initializePermission = async () => {
      await checkPermission();
    };
    initializePermission();
  }, []); // Пустой массив зависимостей — вызов выполняется один раз при монтировании


  return {
    isRecording,
    audioURL,
    startRecording,
    stopRecording,
    setAudioURL
  };
};

export default useAudioRecorder;
