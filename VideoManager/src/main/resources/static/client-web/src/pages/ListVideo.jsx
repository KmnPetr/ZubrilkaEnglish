import React, {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/ListVideo.css';
import {downloadListVideoInfo} from "../services/videoInfoService";
import {useSelector} from "react-redux";
import CreateVideoButton from "../components/listVideo/CreateVideoButton"
import VideoInfoItem from '../components/listVideo/VideoInfoItem';
import {deleteVideoInfo} from "../services/videoInfoService"

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
        console.log("onDeleteVideoInfo    " + videoInfoUuid)
        deleteVideoInfo(videoInfoUuid)
        .then(response => {
            console.log("Удаление видео прошло успешно. Uuid: " + videoInfoUuid)
            downloadList();
        })
        .catch(error => {
            console.log("Ошибка при попытке удаления видео. Uuid: " + videoInfoUuid)
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