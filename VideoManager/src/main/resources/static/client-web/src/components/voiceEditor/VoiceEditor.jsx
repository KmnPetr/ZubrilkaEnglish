import React, { useState } from 'react';
import Prism from 'prismjs';
import ModalWindow from '../ui/ModalWindow';
import { useDispatch, useSelector } from 'react-redux';
import './voiceEditor.css';
import CButton from '../ui/CButton';
import { PHRASE } from '../../store/reducers/jsonEditorReducer';
import { closeVoiceEditor } from '../../store/reducers/voiceEditorReducer';
import { HiOutlineSpeakerWave } from "react-icons/hi2";
import { HiOutlineSpeakerXMark } from "react-icons/hi2";
import { HiOutlineMicrophone } from "react-icons/hi";
import { TbPointFilled } from "react-icons/tb";

/**
 * модальное окно для редактирования озвучки у фразы или слова
 */
const VoiceEditor = () => {
  const dispatch = useDispatch();
  const {isOpen,idPhrase,indexWord,typeObject,str,strLang} = useSelector((state) => state.voiceEditorReducer);
  const [voiseUuid,setVoiceUuid] = useState(null)
  const [isRecording,setIsRecording] = useState(false)

  const onClose = () => dispatch(closeVoiceEditor()); // Закрывает окно

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
                <HiOutlineMicrophone className={`clickable microphone ${isRecording ? 'recording' : ''}`} onClick={()=>setIsRecording(!isRecording)}/>
                {isRecording && <TbPointFilled className='red_point'/>}
            </div>
        </div>
      </div>
    </ModalWindow>
  );
};

export default VoiceEditor;