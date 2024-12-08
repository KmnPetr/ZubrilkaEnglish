import React, { useState, useEffect, useRef } from 'react';
import { useDispatch, useSelector } from "react-redux";
import { setToPauseFalse, setToPlayFalse } from "../../../store/reducers/videoManagementReducer";
import "./VideoComponent.css"

const Video = ({ videoPath }) => {
    const dispatch = useDispatch();
    const videoManagement = useSelector(state => state.videoManagementReducer.videoManagement);
    const videoRef = useRef(null); // Реф для видео
    const [currentTimeMs, setCurrentTimeMs] = useState(0); // Текущее время в миллисекундах
    const [isPlaying, setIsPlaying] = useState(false); // Состояние для управления воспроизведением
    const [videoDurationMs, setVideoDurationMs] = useState(0); // Длительность видео в миллисекундах


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
        const newTime = Number(event.target.value);
        const video = videoRef.current;
        if (video) {
            video.currentTime = newTime / 1000;
            setCurrentTimeMs(newTime);
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


    // Обработчик события loadedmetadata
    const handleMetadataLoaded = () => {
        const video = videoRef.current;
        if (video) {
            setVideoDurationMs(video.duration * 1000); // Устанавливаем длительность видео
        }
    };

    return (
        <div>
            {/* Видео */}
            <video
                ref={videoRef}
                onTimeUpdate={handleTimeUpdate}
                onLoadedMetadata={handleMetadataLoaded}
                onClick={handlePlayPause} // Клик по видео запускает/останавливает
            >
                <source src={videoPath} type="video/mp4" />
                Ваш браузер не поддерживает видео-тег.
            </video>

            {/* Панель управления */}
            <div className='videoControlPanel'>
                {/* Ползунок времени */}
                <input
                    type="range"
                    min="0"
                    max={videoDurationMs || 0}
                    step="100"
                    value={currentTimeMs}
                    onChange={handleSeek}
                    className="timeSlider"
                />

                <div className="controlRow">
                    {/* Кнопка Play/Pause */}
                    <button onClick={handlePlayPause} className="playPauseButton">
                        {isPlaying ? '❚❚ Pause' : '► Play'}
                    </button>
                    {/* Текущее время */}
                    <span className="timeDisplay">{Number(currentTimeMs || 0).toFixed(0)} ms</span>
                </div>
            </div>
        </div>
    );
};

export default Video;