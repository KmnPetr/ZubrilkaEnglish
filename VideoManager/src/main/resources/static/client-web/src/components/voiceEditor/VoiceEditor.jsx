import { useEffect, useState } from 'react';
import ModalWindow from '../../ui/ModalWindow';
import { useDispatch, useSelector } from 'react-redux';
import './voiceEditor.css';
import { closeVoiceEditor } from '../../redux/reducers/voiceEditorReducer';
import { HiOutlineScissors } from "react-icons/hi2";
import { HiOutlineMicrophone } from "react-icons/hi";
import { TbPointFilled } from "react-icons/tb";
import useAudioRecorder from './useAudioRecorder';
import ProgressSlider from './progressSlider/ProgressSlider';
import { CiPlay1,CiPause1 } from "react-icons/ci";
import { useAudioPlayer } from './useAudioPlayer';
import { useDuration } from './useDuration';
import useInterval from './useInterval';
import Zvukogram from './zvukogram/Zvukogram';
import FolderHandler from './folderHandler/FolderHandler';
import { convertMp3ToWav } from '../../utils/audioConverter';
import CButton from '../../ui/CButton';
import { saveWavVoiceOnServer } from '../../api/voiceService';
import {PHRASE, setAudioUuidToPhrases, WORD} from '../../redux/reducers/phraseReduser';
import StrFlipper from "./strFlipper/StrFlipper.jsx";
import PlayVoice, {large} from "../../ui/playVoice/PlayVoice.jsx";
import ListOldVoice from "./listOldVoice/ListOldVoice.jsx";

/**
 * модальное окно для редактирования озвучки у фразы или слова
 */
const VoiceEditor = () => {
  const dispatch = useDispatch();
  const {isOpen,idPhrase,indexWord,typeStr,str,language} = useSelector((state) => state.voiceEditorReducer);
  const phrases = useSelector((state) => state.phraseReducer.phrases);
  const [voiceUuid,setVoiceUuid] = useState(null)
  const {isRecording,audioURL,startRecording,stopRecording,setAudioURL} = useAudioRecorder()
  const {duration} = useDuration(audioURL)
  const {startTime,endTime,changeInterval,maxValue} = useInterval(duration)
  const { play, pause, isPlaying, getTrimmedAudio } = useAudioPlayer(audioURL,startTime,endTime)
  const [audioUrlMp3,setAudioUrlMp3] = useState(null) //содержит ссылку на аудио взятое например из звукограмма
  const [lastUsedVoice,setLastUsedVoice] = useState(null/*{voice: '',sex:''}*/)//последний использованный для озвучки голос нужен для дополнительной информации при сохранении звука на сервере
  const user = useSelector((state) => state.authReducer.user)

    //займется поиском uuid звука
    useEffect(()=>{
        let uuid = null
        if(phrases){
            const phrase = phrases.find(phrase=> phrase.id === idPhrase);
            if(typeStr===PHRASE){
                uuid = phrase[language]?.voice_uuid ?? null
            } else if(typeStr===WORD){
                uuid = phrase.words[indexWord][language]?.voice_uuid ?? null
            }
        }
        setVoiceUuid(uuid)
    },[idPhrase,indexWord,typeStr,language,phrases])

  //при получении ссылки на mp3 например из звукограмма переформатирует его в wav
  useEffect(()=>{
    convertMp3ToWav(audioUrlMp3)
    .then(vawUrl=>{
      setAudioURL(vawUrl)
    })
  },[audioUrlMp3])

  const onClose = () => dispatch(closeVoiceEditor()); // Закрывает окно
  const trimAudio=()=> getTrimmedAudio().then(newURL=>setAudioURL(newURL))

  const apply =()=>{
    saveWavVoiceOnServer(audioURL,str,lastUsedVoice)
    .then(voiceUuid=>{
      dispatch(setAudioUuidToPhrases(voiceUuid,typeStr,idPhrase,indexWord,language))
    })
  }

  const onClickRecord=()=> {
      !isRecording ? startRecording() : stopRecording()
      setLastUsedVoice({voice:user.short_name,sex:user.sex})
  }

    //присвоит строке voice_uuid из старых ранее созданных voice
    const onSelectOldVoice=(uuid)=>{console.log('Select old uuid ', uuid)}

  return (
    <ModalWindow isOpen={isOpen} onClose={onClose} width="70%" height="70%">
      <div className="voice-editor-container">
        <div className="button-container">
        </div>
        <div className="voice-editor-wrapper">
            <div style={{display:"flex",flexDirection:"row",alignItems:"center"}}>
                <h1>{"\""+str+"\""}</h1>
                <PlayVoice voiceUuid={voiceUuid} size={large}/>
            </div>

            <ListOldVoice str={str} onSelectOldVoice={onSelectOldVoice}/>

            <div className='record_box'>
                <HiOutlineMicrophone className={`clickable microphone2 ${isRecording ? 'recording' : ''}`} onClick={onClickRecord}/>
                {isRecording && <TbPointFilled className='red_point'/>}
                {!isPlaying ? <CiPlay1 className='clickable play_pause' onClick={play}/> : <CiPause1 className='clickable play_pause' onClick={pause}/>}
                <HiOutlineScissors className='clickable trim_i' onClick={trimAudio}/>
                <p>duration: {duration}</p>

                <ProgressSlider onChenge={changeInterval} maxValue={maxValue} changedSignal={audioURL}/>
                <p>startTime: {startTime}</p>
                <p>endTime: {endTime}</p>
                <FolderHandler onSelectAudio={(urlAudio)=>setAudioURL(urlAudio)}/>
            </div>
            <Zvukogram onChangeAudioUrlMp3={setAudioUrlMp3} setLastUsedVoice={setLastUsedVoice}/>
            <CButton onClick={apply}>Apply voice</CButton>
        </div>
          <StrFlipper className='str_flipper'/>
      </div>
    </ModalWindow>
  );
};

export default VoiceEditor;