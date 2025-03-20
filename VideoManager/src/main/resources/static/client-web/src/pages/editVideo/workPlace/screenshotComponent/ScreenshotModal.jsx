import React from 'react';
import './Screenshot.css';
import { FiX } from "react-icons/fi";
import { sendIconToServer } from "../../../../api/iconService.js"
import {useDispatch} from "react-redux";
import {iconWasChanged} from "../../../../redux/reducers/networkReducer.js"

const ScreenshotModal = ({ screenshot, onClose,videoInfo_uuid }) => {
    const dispatch = useDispatch();

  const handleSaveImage = () => {
    const link = document.createElement('a');
    link.href = screenshot; // Указываем URL скриншота
    link.download = 'screenshot.png'; // Имя сохраняемого файла
    link.click();
  };


  const handleOpenGoogleTranslate = () => {
    handleSaveImage()
    // Открытие страницы Google Translate
    window.open('https://translate.google.com/?sl=zh-CN&tl=ru&op=images', '_blank');
  };

  const handleOpenYandexTranslate = () => {
    handleSaveImage()
    // Открытие страницы Yandex Translate
    window.open('https://translate.yandex.ru/ocr', '_blank');
  };
  const setAsIcon = () => {
    sendIconToServer(screenshot,videoInfo_uuid)
      .then(()=>dispatch(iconWasChanged()))
    onClose()
  };

  return (
    <div className="overlay" onClick={onClose}>
      <div className="dialog">
        <div style={{display: 'flex', justifyContent:'end',fontSize: '30px'}}>
          <FiX onClick={onClose} className='clickable'/>
        </div>
        <div>
          <img src={screenshot} alt="Screenshot" className="screenshot-modal-image" />
        </div>
        <div className='custom-button-container'>
          <button onClick={handleSaveImage}> Сохранить </button>
          <button onClick={handleOpenGoogleTranslate} style={{backgroundColor:'#4c83b1'}}>Google Translate</button>
          <button onClick={handleOpenYandexTranslate} style={{backgroundColor:'#b8a33c'}}>Yandex Translate</button>
          <button onClick={setAsIcon}>Set as an icon</button>
        </div>
      </div>
    </div>
  );
};

export default ScreenshotModal;