import React from 'react';
import './cButton.css'; // Подключаем стили

//сделано наподобие https://coreui.io/react/docs/components/button/#outline-buttons
const CButton = ({ text, onClick }) => {
  return (
    <button className="custom-button" onClick={onClick}>
      {text}
    </button>
  );
};

export default CButton;