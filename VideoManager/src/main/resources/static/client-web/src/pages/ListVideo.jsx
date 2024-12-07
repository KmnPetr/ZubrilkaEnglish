import React, {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/ListVideo.css';
import {downloadListVideo} from "../services/videoService";
import {useSelector} from "react-redux";

const ListVideo = () => {
    const navigate = useNavigate();
    const user = useSelector(state => state.authReducer.user)
    const [videos, setVideos] = useState([]); // Состояние для хранения списка видео
    const [loading, setLoading] = useState(true); // Состояние для отображения загрузки
    const [error, setError] = useState(null); // Состояние для хранения ошибки



    useEffect(() => {

        downloadListVideo(user.refreshToken)
            .then(response => {
                setVideos(response);
                setLoading(false); // Останавливаем индикацию загрузки
            })
            .catch(error => {
                setError(error.message); // Сохраняем ошибку, если запрос не удался
                setLoading(false); // Останавливаем индикацию загрузки
            });
    }, []); // Пустой массив зависимостей означает, что эффект сработает один раз при монтировании

    const handleVideoClick = (videoInfoUuid) => {
        // Переход на страницу обработки видео с параметром videoId
        navigate(`/editVideo/${videoInfoUuid}`);
    };


    if (loading) {
        return <div className="loading">Loading...</div>;
    }
    if (error) {
        return <div className="error">Error: {error}</div>;
    }
    return (
        <div className="list-video-container">
            <h1 className="list-video-title">Video List</h1>
            <ul className="video-list">
                {videos.map((video) => (
                    <li key={video.uuid} className="video-item">
                        <h2>{video.cnName}</h2>
                        <h2>{video.enName}</h2>
                        <h2>{video.ruName}</h2>
                        <p>Переводчик: {video.translator_name}</p>
                        <p>{video.description}</p>
                        <button className="view-button" onClick={() => handleVideoClick(video.uuid)}>
                            Edit Video
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ListVideo;