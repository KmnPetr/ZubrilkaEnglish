
import React, { useState, useEffect, useRef } from 'react';
import {useDispatch, useSelector} from "react-redux";
import {setToPauseFalse, setToPlayFalse} from "../../../store/reducers/videoManagementReducer";

/**
 * Компонент для отображения видео, над которым идет работа
 */

const Video = ({ videoPath }) => {
    const dispatch = useDispatch();
    const videoManagement = useSelector(state => state.videoManagementReducer.videoManagement);
    const videoRef = useRef(null); // Создаем реф для видео
    const [currentTimeMs, setCurrentTimeMs] = useState(0); // Хранение времени воспроизведения в миллисекундах

    const playPhraseInterval = () => {
        const video = videoRef.current;
        if (videoManagement.phraseInterval.startTime !== null && videoManagement.phraseInterval.toPlay) {
            if (video) {
                video.currentTime = videoManagement.phraseInterval.startTime/1000;
                video.play();
                dispatch(setToPlayFalse())
            }
        }
    };

    // отследит состояние обьекта videoManagement в редаксе
    useEffect(() => {
        playPhraseInterval();
    }, [videoManagement]);

    // Хук useEffect для обновления времени и вызова старт/паузы
    useEffect(() => {
        const interval = setInterval(() => {
            const video = videoRef.current;
            if (video) {
                setCurrentTimeMs(video.currentTime * 1000); // Обновляем текущее время в миллисекундах
            }
        }, 100);

        return () => {
            clearInterval(interval); // Очищаем интервал при размонтировании компонента
        };
    }, []);

    // Обработчик для обновления текущего времени
    const handleTimeUpdate = (e) => {
        onIntervalPause(e.target.currentTime * 1000);
    };

    const onIntervalPause = (currentTimeVideo) => {
        if (currentTimeVideo>=videoManagement.phraseInterval.endTime&&videoManagement.phraseInterval.toPause){
            const video = videoRef.current;
            if (video){
                video.pause();
                dispatch(setToPauseFalse());
            }
        }
    }

    return (
        <div>
            <video
                ref={videoRef}
                width="600"
                controls
                onTimeUpdate={handleTimeUpdate}>
                <source src={videoPath} type="video/mp4" />
                Ваш браузер не поддерживает видео-тег.
            </video>
            <p>Текущее время: {currentTimeMs.toFixed(0)} ms</p>
        </div>
    );
};

export default Video;