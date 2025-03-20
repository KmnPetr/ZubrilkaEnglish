import ListPhrases from "./listPhrases/ListPhrases.jsx";
import VideoComponent from "./videoComponent/VideoComponent.jsx";
import VideoInfo from "./videoInfo/VideoInfo.jsx"
import "./WorkPlace.css";
import ScreenshotComponent from "./screenshotComponent/ScreenshotComponent.jsx"
import JsonEditor from './jsonEditor/JsonEditor.jsx';
import VoiceEditor from './voiceEditor/VoiceEditor.jsx';
import CardEditor from "./cardEditor/CardEditor.jsx";

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
            <VoiceEditor/>
            <CardEditor/>
        </div>
    );
};

export default WorkPlace;