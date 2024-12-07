import React from 'react';
import ListPhrases from "./listPhrases/ListPhrases";
import VideoComponent from "./videoComponent/VideoComponent";
import VideoInfo from "./videoInfo/VideoInfo"
import "../../css/WorkPlace.css";

const WorkPlace = ({ translation_uuid, video_uuid, videoInfo_uuid,reloadVideoInfo }) => {
    return (
        <div>
            <div className="workPlace">
                <VideoInfo videoInfo_uuid={videoInfo_uuid}/>
                <ListPhrases translation_uuid={translation_uuid}/>
                <VideoComponent
                    video_uuid={video_uuid}
                    videoInfo_uuid={videoInfo_uuid}
                    reloadVideoInfo={reloadVideoInfo}/>
            </div>
        </div>
    );
};

export default WorkPlace;