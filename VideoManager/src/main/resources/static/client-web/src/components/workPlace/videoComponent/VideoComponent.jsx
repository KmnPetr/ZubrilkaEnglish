import React, {useEffect} from "react";
import SelectFileButton from "./SelectFileButton";
import Video from "./Video";
import {useDispatch, useSelector} from "react-redux";
import {downloadVideoByUUID, uploadVideo} from "../../../services/videoService";
import "./VideoComponent.css"

/**
 * Компонент для отображения видео, над которым идет работа
 */
const VideoComponent = ({video_uuid,videoInfo_uuid,reloadVideoInfo}) => {
    const dispatch = useDispatch();
    const videoPath = useSelector(state => state.videoManagementReducer.videoManagement.videoPath);
    const network = useSelector(state => state.networkReducer.network);
    const video = useSelector(state => state.videoReducer.video);

    const sendVideoToServer = (videoPath) => {
        uploadVideo(videoPath, videoInfo_uuid, dispatch).then(r  =>reloadVideoInfo())
    }
    useEffect(() => {
        if (video_uuid!==null){
            downloadVideoByUUID(video_uuid,dispatch)
        }
    },[video_uuid])

    const onSelectVideo = (videoURL) => {
        sendVideoToServer(videoURL);
    }
    return (
        <div className="videoComponent">

            {!video_uuid ? (<div>
                <p>The video has not been installed yet</p>
                    <SelectFileButton onSelectVideo={onSelectVideo}/>
                    <p>{network.uploadVideoProgress}</p>
            </div>) : (
                <div>

                    {!video ? (
                        <div>
                            <p>Download video... {network.downloadVideoProgress}</p>
                        </div>
                    ) : (
                        <div>
                            <Video videoPath={video.videoUrl}/>
                        </div>
                    )}
                </div>
            )}


        </div>
    );
};

export default VideoComponent;