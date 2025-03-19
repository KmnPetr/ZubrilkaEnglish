import React from 'react';
import './cButton.css'; // Подключаем стили

//сделано наподобие https://coreui.io/react/docs/components/button/#outline-buttons
const CButton = ({ text, onClick,color,children,className }) => {
  return (
    <button className={`custom-button ${className}`} onClick={onClick} style={{color:`${color}`,borderColor:`${color}`}}>
      {text}
      {children}
    </button>
  );
};

export default CButton;