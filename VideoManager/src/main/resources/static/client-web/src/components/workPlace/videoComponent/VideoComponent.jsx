import React, {useEffect} from "react";
import SelectFileButton from "./SelectFileButton";
import Video from "./Video";
import {useDispatch, useSelector} from "react-redux";
import {downloadVideoByUUID, uploadVideo} from "../../../api/videoService";
import "./VideoComponent.css"
import {clearVideo} from "../../../redux/reducers/videoReducer"
import {clearVideoProgress} from "../../../redux/reducers/networkReducer"

/**
 * Компонент для отображения видео, над которым идет работа
 */
const VideoComponent = ({videoInfo_uuid,reloadVideoInfo}) => {
    const dispatch = useDispatch();
    const videoPath = useSelector(state => state.videoManagementReducer.videoManagement.videoPath);
    const network = useSelector(state => state.networkReducer.network);
    const video = useSelector(state => state.videoReducer.video);
    const isVideoExist = useSelector(state => state.videoReducer.video.isExist);


    useEffect(() => {
        dispatch(clearVideo())//очистиит старые данные по видео из редакса
        dispatch(clearVideoProgress())
        return () => {
            dispatch(clearVideo())//очистиит старые данные по видео при выходе
            dispatch(clearVideoProgress())
        };
    }, []);

    const sendVideoToServer = (videoPath) => {
        uploadVideo(videoPath, videoInfo_uuid, dispatch).then(r  => reloadVideoInfo())
    }
    useEffect(() => {
        if (videoInfo_uuid!==null&&isVideoExist){
            downloadVideoByUUID(videoInfo_uuid,dispatch)
        }
    },[videoInfo_uuid,isVideoExist])

    const onSelectVideo = (videoURL) => {
        sendVideoToServer(videoURL);
    }
    return (
        <div className="videoComponent">

            {!video.isExist ? (<div>
                <p>The video has not been installed yet</p>
                    <SelectFileButton onSelectVideo={onSelectVideo}/>
                    <p>{network.uploadVideoProgress}</p>
            </div>) : (
                <div>

                    {!video.videoUrl ? (
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