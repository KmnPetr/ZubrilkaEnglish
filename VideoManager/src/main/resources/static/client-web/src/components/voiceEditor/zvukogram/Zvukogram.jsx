import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import './zvukogram.css'
import CButton from '../../../ui/CButton'
import { copyToClipboard } from '../../../utils/copyToClipboard';
import SelectVoice from './selectVoice/SelectVoice';
import { FormControlLabel,Checkbox } from '@mui/material';
import { synthesizeSpeech } from '../../../api/zvukogramService';

//сделано наподобие https://coreui.io/react/docs/components/button/#outline-buttons
const Zvukogram = ({onChangeAudioUrlMp3,setLastUsedVoice}) => {
    const {str,language} = useSelector((state) => state.voiceEditorReducer);
    const [voice,setVoice] = useState(null/*{voice:'',sex:''}*/);
    const [useApi, setUseApi] = useState(true);

    const onClickZvButton =()=>{
      if(useApi){
        synthesizeSpeech(voice.voice,str)
        .then(localUrl=>{
          onChangeAudioUrlMp3(localUrl)
            setLastUsedVoice(voice)
        })
        .catch()
      } else{
        goToZvukogram()
      }
    }
    //перенаправит на страницу звукограмма, скопирует строку
    const goToZvukogram=()=>{
        copyToClipboard(str)
        redirectToZvukogram(language)
    }

  return (
    <div className='box'>
        <CButton color='#d3c97e' className='clickable' onClick={onClickZvButton}>Zvukogram</CButton>
        <SelectVoice onSelect={setVoice} language={language}/>

        <FormControlLabel control={ <Checkbox checked={useApi} onChange={(e)=>setUseApi(e.target.checked)} /> } label="use api" />
    </div>
  );
};

export default Zvukogram;


function redirectToZvukogram(language) {
    const url_cn = 'https://zvukogram.com/speech/chinese/'
    const url_en = "https://zvukogram.com/speech/tts-english/";
    const url_ru = 'https://zvukogram.com/speech/rus/'
  
    let url = 'https://zvukogram.com/';
  
    switch (language) {
      case 'cn':
        url = url_cn
        break;
      case 'en':
        url = url_en
        break;
      case 'ru':
        url = url_ru
        break;
      default:
        break;
    }
  
    // Открываем URL в новой вкладке
    window.open(url, '_blank');
  }