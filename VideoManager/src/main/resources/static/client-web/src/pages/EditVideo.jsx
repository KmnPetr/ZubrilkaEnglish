import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import WorkPlace from "../components/workPlace/WorkPlace";
import {getVideoInfoByUuid} from "../services/videoService";

/**
 * the component is a workplace for video editing, more precisely, for the formation of its translation
 */
const EditVideo = () => {
    const { videoUuid } = useParams();
    const [videoInfo, setVideoInfo] = useState(null);

    //make a request to the server and accept the VideoInfo object
    useEffect(() => {
        if (videoInfo==null){
            getVideoInfoByUuid(videoUuid)
                .then(response => setVideoInfo(response))
                .catch(error => console.log(error));
        }
    }, [videoUuid]);

    const reloadVideoInfo = () => {getVideoInfoFServ(videoUuid)}

    const getVideoInfoFServ = (videoUuid) => {
            getVideoInfoByUuid(videoUuid)
                .then(response => setVideoInfo(response))
                .catch(error => console.log(error));
    }


    return (<div className="name">
            {videoInfo === null ? (
                <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh'}}>
                    Загрузка...
                </div>
            ) : (
                <WorkPlace
                    translation_uuid={videoInfo.translation_uuid}
                    video_uuid={videoInfo.video_uuid}
                    videoInfo_uuid={videoInfo.uuid}
                    reloadVideoInfo={reloadVideoInfo}/>
            )}
        </div>
    );
};

export default EditVideo;