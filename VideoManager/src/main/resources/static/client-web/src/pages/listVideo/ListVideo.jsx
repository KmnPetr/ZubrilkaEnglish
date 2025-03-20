import React, {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import './ListVideo_2.css';
import {downloadListVideoInfo} from "../../api/videoInfoService.js";
import {useSelector} from "react-redux";
import CreateVideoButton from "./createVideoButton/CreateVideoButton.jsx"
import VideoInfoItem from './videoInfoItem/VideoInfoItem.jsx';
import {deleteVideoInfo} from "../../api/videoInfoService.js"

const ListVideo = () => {
    const navigate = useNavigate();
    const user = useSelector(state => state.authReducer.user)
    const [videos, setVideos] = useState([]); // Состояние для хранения списка видео
    const [loading, setLoading] = useState(true); // Состояние для отображения загрузки
    const [error, setError] = useState(null); // Состояние для хранения ошибки

    const downloadList =()=> {
        downloadListVideoInfo()
            .then(response => {
                setVideos(response);
                setLoading(false); // Останавливаем индикацию загрузки
            })
            .catch(error => {
                setError(error.message); // Сохраняем ошибку, если запрос не удался
                setLoading(false); // Останавливаем индикацию загрузки
            });
    }

    const handleVideoClick = (videoInfoUuid) => {
        // Переход на страницу обработки видео с параметром videoId
        navigate(`/editVideo/${videoInfoUuid}`);
    };

    //вызывается после успешного создания нового видео
    const onCreateNewVideo = () => {
        downloadList();
    }

    //отправляет запрос на удаление видео
    const onDeleteVideoInfo =(videoInfoUuid)=> {
        deleteVideoInfo(videoInfoUuid)
        .then(response => {
            downloadList();
        })
        .catch(error => {
            console.error("Ошибка при попытке удаления видео. Uuid: " + videoInfoUuid)
        });
    }

    useEffect(() => {
        downloadList();
    }, []);


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
                {videos.map((videoInfo) => (
                    <VideoInfoItem videoInfo={videoInfo} key={videoInfo.uuid} handleVideoClick={handleVideoClick} onDelete={onDeleteVideoInfo}/>
                ))}
                <CreateVideoButton onCreateVideo={onCreateNewVideo}/>
            </ul>
        </div>
    );
};

export default ListVideo;