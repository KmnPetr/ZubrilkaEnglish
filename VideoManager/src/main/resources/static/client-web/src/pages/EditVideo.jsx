import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import WorkPlace from "../components/workPlace/WorkPlace";
import {getVideoInfoByUuid} from "../api/videoService";

/**
 * the component is a workplace for video editing, more precisely, for the formation of its translation
 */
const EditVideo = () => {
    const { videoInfoUuid } = useParams();
    const [videoInfo, setVideoInfo] = useState(null);


    //make a request to the server and accept the VideoInfo object
    useEffect(() => {
        reloadVideoInfo()
    }, []);

    const reloadVideoInfo = () => {
        getVideoInfoByUuid(videoInfoUuid)
            .then(response => setVideoInfo(response))
            .catch(error => console.error(error));
    }


    return (
        <div className="name">
            {videoInfo === null ? (
                <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh'}}>
                    Загрузка...
                </div>
            ) : (
                <WorkPlace
                    videoInfo_uuid={videoInfo.uuid}
                    reloadVideoInfo={reloadVideoInfo}/>
            )}
        </div>
    );
};

export default EditVideo;