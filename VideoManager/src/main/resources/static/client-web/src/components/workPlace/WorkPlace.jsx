import React from 'react';
import ListPhrases from "./listPhrases/ListPhrases";
import VideoComponent from "./videoComponent/VideoComponent";

const WorkPlace = ({ translation_uuid, video_uuid, videoInfo_uuid,reloadVideoInfo }) => {
    return (
        <div className="workPlace">
            <ListPhrases translation_uuid={translation_uuid}/>
            <VideoComponent
                video_uuid={video_uuid}
                videoInfo_uuid={videoInfo_uuid}
                reloadVideoInfo={reloadVideoInfo}/>
        </div>
    );
};

export default WorkPlace;