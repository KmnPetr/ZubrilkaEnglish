import React, { useEffect, useState } from 'react';
import "../../../css/WorkPlace.css";
import { getVideoInfoByUuid } from "../../../services/videoService";
import EditInfo from "./EditInfo";
import { updateVideoInfoField } from "../../../services/videoInfoService";
import {downloadIcon} from "../../../services/iconService"
import { useSelector } from "react-redux";

const VideoInfo = ({ videoInfo_uuid }) => {
    const [videoInfo, setVideoInfo] = useState(null); // Состояние для хранения видеоинформации
    const [loading, setLoading] = useState(true); // Состояние для отображения загрузки
    const [error, setError] = useState(null); // Состояние для ошибок
    const [showJson, setShowJson] = useState(false); // Состояние для управления видимостью JSON
    const [imageUrl, setImageUrl] = useState(null);
    const isIconChanged = useSelector(state => state.networkReducer.network.isIconChanged);

        useEffect(() => {
            downloadIcon(videoInfo_uuid)
            .then(imageUrl=>{setImageUrl(imageUrl)})
            .catch(error => {});
        }, [isIconChanged]);

    // Запрос данных при загрузке компонента или изменении videoInfo_uuid
    useEffect(() => {
        getVideoInfoByUuid(videoInfo_uuid)
            .then(response => {
                setVideoInfo(response);
                setLoading(false);
            })
            .catch(err => {
                setError(err.message);
            })
            .finally(() => {
                setLoading(false);
            });
    }, [videoInfo_uuid]);

    const onUpdateVideoInfoField = (fieldName, newValue) => {
        setLoading(true);
        updateVideoInfoField(videoInfo_uuid, fieldName, newValue)
            .then(response => {
                setVideoInfo(response);
                setLoading(false);
            })
            .catch(err => {
                setError(err.message);
            })
            .finally(() => {
                setLoading(false);
            });
    };

    // Функция для переключения состояния видимости JSON
    const toggleJsonVisibility = () => {
        setShowJson(!showJson);
    };

    // Визуализация компонента
    return (
        <div>
            <details>
                <summary>Additional information</summary>
                {/* Состояние загрузки, ошибки или отображение данных */}
                <div>
                    {loading && <p>Загрузка...</p>}
                    {error && <p style={{ color: 'red' }}>Ошибка: {error}</p>}
                    {videoInfo && (
                        <div>
                            <div>
                            {imageUrl && <img src={imageUrl} className="icon"/>}
                            </div>
                            <div>
                                <EditInfo fieldName={'cnName'} fieldNameAlias={'Video cn_name'} fieldValue={videoInfo.cnName} onUpdateVideoInfoField={onUpdateVideoInfoField} />
                                <EditInfo fieldName={'enName'} fieldNameAlias={'Video en_name'} fieldValue={videoInfo.enName} onUpdateVideoInfoField={onUpdateVideoInfoField} />
                                <EditInfo fieldName={'ruName'} fieldNameAlias={'Video ru_name'} fieldValue={videoInfo.ruName} onUpdateVideoInfoField={onUpdateVideoInfoField} />
                                <EditInfo fieldName={'linkOriginal'} fieldNameAlias={'Link to the original'} fieldValue={videoInfo.linkOriginal} onUpdateVideoInfoField={onUpdateVideoInfoField} />
                            </div>
                            <h3 onClick={toggleJsonVisibility} style={{ cursor: 'pointer', textDecoration: 'underline', fontSize: '12px' }}>
                                {showJson ? "Hide detailed video information" : "Show detailed video information"}
                            </h3>
                            {showJson && (
                                <pre>{JSON.stringify(videoInfo, null, 2)}</pre>
                            )}
                        </div>
                    )}
                </div>
            </details>
        </div>
    );
};

export default VideoInfo;