import React from 'react';
import './Screenshot.css';

const ScreenshotModal = ({ screenshot, onClose }) => {
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

  return (
    <div className="screenshot-modal" onClick={onClose}>
      <div
        className="screenshot-modal-content"
        onClick={(e) => e.stopPropagation()}
      >
        <button className="screenshot-modal-close" onClick={onClose}>
          &times;
        </button>

        <img
          src={screenshot}
          alt="Screenshot"
          className="screenshot-modal-image"
        />
      </div>

      <div className="screenshot-modal-footer">
        <button className="screenshot-modal-save" onClick={handleSaveImage}>
          Сохранить
        </button>
      </div>

      <button
          className="screenshot-modal-save"
          onClick={handleOpenGoogleTranslate}
        >
          Открыть Google Translate
        </button>
    </div>
  );
};

export default ScreenshotModal;