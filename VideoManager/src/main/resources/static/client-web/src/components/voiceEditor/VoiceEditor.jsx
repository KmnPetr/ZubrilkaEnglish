import React, { useEffect, useState } from 'react';
import ModalWindow from '../ui/ModalWindow';
import { useDispatch, useSelector } from 'react-redux';
import './voiceEditor.css';
import CButton from '../ui/CButton';
import { PHRASE } from '../../store/reducers/jsonEditorReducer';
import { closeVoiceEditor } from '../../store/reducers/voiceEditorReducer';
import { HiOutlineSpeakerWave,HiOutlineSpeakerXMark,HiOutlineScissors } from "react-icons/hi2";
import { HiOutlineMicrophone } from "react-icons/hi";
import { TbPointFilled } from "react-icons/tb";
import useAudioRecorder from './useAudioRecorder';
import ProgressSlider from './progressSlider/ProgressSlider';
import { CiPlay1,CiPause1 } from "react-icons/ci";
import { useAudioPlayer } from './useAudioPlayer';
import { useDuration } from './useDuration';
import useInterval from './useInterval';

/**
 * модальное окно для редактирования озвучки у фразы или слова
 */
const VoiceEditor = () => {
  const dispatch = useDispatch();
  const {isOpen,idPhrase,indexWord,typeObject,str,strLang} = useSelector((state) => state.voiceEditorReducer);
  const [voiseUuid,setVoiceUuid] = useState(null)
  const {isRecording,audioURL,startRecording,stopRecording,setAudioURL} = useAudioRecorder()
  const {duration} = useDuration(audioURL)
  const {startTime,endTime,changeInterval,maxValue} = useInterval(duration)
  const { play, pause, isPlaying, getTrimmedAudio } = useAudioPlayer(audioURL,startTime,endTime)

  const onClose = () => dispatch(closeVoiceEditor()); // Закрывает окно
  const trimAudio=()=> getTrimmedAudio().then(newURL=>setAudioURL(newURL))

  const apply =()=>{
    try {
      switch (typeObject) {
        case PHRASE:
          //dispatch(updatePhrase(convertJsonToPhrase(jsonObject,editableJson,nativeLang)))
          onClose()
          break;
        default:console.error("Invalid typeObject")
      }
    } catch (error) {
      //console.error('Invalid JSON string:', error.message);
    }
  }

  const onClickRecord=()=> !isRecording ? startRecording() : stopRecording()

  return (
    <ModalWindow isOpen={isOpen} onClose={onClose} width="70%" height="70%">
      <div className="voice-editor-container">
        <div className="button-container">
          <CButton text={'Apply'} onClick={apply}/>
        </div>
        <div className="voice-editor-wrapper">
            <h1>{"\""+str+"\""}</h1>
            <div className='speaker_box'>
                {voiseUuid ? 
                <HiOutlineSpeakerWave className='speaker clickable' style={{color:'#31cc5a'}}/> : 
                <HiOutlineSpeakerXMark className='speaker' style={{color:'#82aaff'}}/>}
            </div>
            <div className='record_box'>
                <HiOutlineMicrophone className={`clickable microphone2 ${isRecording ? 'recording' : ''}`} onClick={onClickRecord}/>
                {isRecording && <TbPointFilled className='red_point'/>}
                {!isPlaying ? <CiPlay1 className='clickable play_pause' onClick={play}/> : <CiPause1 className='clickable play_pause' onClick={pause}/>}
                <HiOutlineScissors className='clicable trim_i' onClick={trimAudio}/>
                <p>duration: {duration}</p>

                <ProgressSlider onChenge={changeInterval} maxValue={maxValue} changedSignal={audioURL}/>
                <p>startTime: {startTime}</p>
                <p>endTime: {endTime}</p>
            </div>
        </div>
      </div>
    </ModalWindow>
  );
};

export default VoiceEditor;