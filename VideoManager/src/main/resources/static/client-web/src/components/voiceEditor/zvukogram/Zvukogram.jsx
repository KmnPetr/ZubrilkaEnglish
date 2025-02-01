import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import './zvukogram.css'
import CButton from '../../ui/CButton'
import { copyToClipboard } from '../../../utils/copyToClipboard';
import SelectVoice from './selectVoice/SelectVoice';

//сделано наподобие https://coreui.io/react/docs/components/button/#outline-buttons
const Zvukogram = () => {
    const {isOpen,idPhrase,indexWord,typeObject,str,language} = useSelector((state) => state.voiceEditorReducer);
    const [voice,setVoice] = useState(null);
    


    const goToZvukogram=()=>{
        copyToClipboard(str)
        redirectToZvukogram(language)
    }
  return (
    <div className='box'>
        <CButton color='#d3c97e' className='clickable' onClick={goToZvukogram}>Zvukogram</CButton>
        <SelectVoice onSelect={setVoice} language={language}/>
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
    window.open(/*finalUrl*/url, '_blank');
  }