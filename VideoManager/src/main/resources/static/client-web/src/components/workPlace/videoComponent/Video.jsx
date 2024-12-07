import React, { useState, useEffect, useRef } from 'react';
import { useDispatch, useSelector } from "react-redux";
import { setToPauseFalse, setToPlayFalse } from "../../../store/reducers/videoManagementReducer";

const Video = ({ videoPath }) => {
    const dispatch = useDispatch();
    const videoManagement = useSelector(state => state.videoManagementReducer.videoManagement);
    const videoRef = useRef(null); // Реф для видео
    const [currentTimeMs, setCurrentTimeMs] = useState(0); // Текущее время в миллисекундах
    const [isPlaying, setIsPlaying] = useState(false); // Состояние для управления воспроизведением

    const playPhraseInterval = () => {
        const video = videoRef.current;
        if (videoManagement.phraseInterval.startTime !== null && videoManagement.phraseInterval.toPlay) {
            if (video) {
                video.currentTime = videoManagement.phraseInterval.startTime / 1000;
                video.play();
                setIsPlaying(true);
                dispatch(setToPlayFalse());
            }
        }
    };

    // Отслеживаем изменения videoManagement
    useEffect(() => {
        playPhraseInterval();
    }, [videoManagement]);

    // Обновляем текущее время через интервал
    useEffect(() => {
        const interval = setInterval(() => {
            const video = videoRef.current;
            if (video) {
                setCurrentTimeMs(video.currentTime * 1000); // Обновляем время в миллисекундах
            }
        }, 100);

        return () => {
            clearInterval(interval); // Очищаем интервал
        };
    }, []);

    const handlePlayPause = () => {
        const video = videoRef.current;
        if (video) {
            if (isPlaying) {
                video.pause();
            } else {
                video.play();
            }
            setIsPlaying(!isPlaying);
        }
    };

    const handleTimeUpdate = (e) => {
        const currentTime = e.target.currentTime * 1000;
        setCurrentTimeMs(currentTime);
        onIntervalPause(currentTime);
    };

    const handleSeek = (event) => {
        const newTime = event.target.value;
        const video = videoRef.current;
        if (video) {
            video.currentTime = newTime / 1000;
            setCurrentTimeMs(newTime); // Обновляем время
        }
    };

    const onIntervalPause = (currentTimeVideo) => {
        if (
            currentTimeVideo >= videoManagement.phraseInterval.endTime &&
            videoManagement.phraseInterval.toPause
        ) {
            const video = videoRef.current;
            if (video) {
                video.pause();
                setIsPlaying(false);
                dispatch(setToPauseFalse());
            }
        }
    };

    return (
        <div style={{ width: '600px', margin: '0 auto' }}>
            {/* Видео */}
            <video
                ref={videoRef}

                width="600"
                onTimeUpdate={handleTimeUpdate}
                style={{ cursor: 'pointer', display: 'block', marginBottom: '10px' }}
                onClick={handlePlayPause} // Клик по видео запускает/останавливает
            >
                <source src={videoPath} type="video/mp4" />
                Ваш браузер не поддерживает видео-тег.
            </video>

            {/* Панель управления */}
            <div
                style={{
                    background: 'rgba(0, 0, 0, 0.7)',
                    color: 'white',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                    padding: '10px',
                    borderRadius: '5px',
                }}
            >
                {/* Кнопка Play/Pause */}
                <button onClick={handlePlayPause} style={{ background: 'none', border: 'none', color: 'white' }}>
                    {isPlaying ? '❚❚ Pause' : '► Play'}
                </button>

                {/* Ползунок времени */}
                <input
                    type="range"
                    min="0"
                    max={videoRef.current?.duration * 1000 || 0}
                    step="100"
                    value={currentTimeMs}
                    onChange={handleSeek}
                    style={{ flex: 1, margin: '0 10px' }}
                />

                {/* Текущее время */}
                <span>{currentTimeMs.toFixed(0)} ms</span>
            </div>
        </div>
    );
};

export default Video;