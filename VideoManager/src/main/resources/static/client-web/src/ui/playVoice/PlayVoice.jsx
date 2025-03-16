import './playVoice.css';
import {HiOutlineSpeakerWave, HiOutlineSpeakerXMark} from "react-icons/hi2";
import {downloadVoiceAsMp3} from "../../api/voiceService.js";
import {playAudio} from "../../utils/audioUtil.jsx";

export const small = 'small_speaker'
export const medium = 'medium_speaker'
export const large = 'large_speaker'

const PlayVoice = ({className, style, voiceUuid, size}) => {
    const playVoice = () => {
        downloadVoiceAsMp3(voiceUuid).then(
            voiceUrl => {playAudio(voiceUrl).onEnd(() => {})}
        )
    }
    return (
        <div className={`speaker_box ${className}`} style={style}>
            {voiceUuid ?
                <HiOutlineSpeakerWave className={`clickable ${size}`} style={{color:'#31cc5a'}} onClick={playVoice}/> :
                <HiOutlineSpeakerXMark className={`${size}`} style={{color:'#e8e33f'}}/>}
    </div>)
}
export default PlayVoice;