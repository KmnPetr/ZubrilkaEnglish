import React from 'react';
import ListPhrases from "./listPhrases/ListPhrases";
import VideoComponent from "./videoComponent/VideoComponent";
import VideoInfo from "./videoInfo/VideoInfo"
import "../../css/WorkPlace.css";
import ScreenshotComponent from "../screenshotComponent/ScreenshotComponent"
import JsonEditor from '../jsonModal/JsonEditor';

const WorkPlace = ({ videoInfo_uuid,reloadVideoInfo }) => {
    return (
        <div>
            <div className="workPlace">
                <VideoInfo videoInfo_uuid={videoInfo_uuid}/>
                <ListPhrases videoInfo_uuid={videoInfo_uuid}/>
                <VideoComponent
                    videoInfo_uuid={videoInfo_uuid}
                    reloadVideoInfo={reloadVideoInfo}/>
            </div>
            <ScreenshotComponent videoInfo_uuid={videoInfo_uuid}/>
            <JsonEditor/>
        </div>
    );
};

export default WorkPlace;