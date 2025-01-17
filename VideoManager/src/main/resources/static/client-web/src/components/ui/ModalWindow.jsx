import React from 'react';
import { FiX } from 'react-icons/fi';
import './ModalWindow.css';

const ModalWindow = ({ isOpen, onClose, children, width = '50%', height = '50%' }) => {
  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-dialog" onClick={(e) => e.stopPropagation()} style={{width,height,}}>
        <FiX onClick={onClose} className="close-icon clickable" />
        <div className="modal-content">
          {children}
        </div>
      </div>
    </div>
  );
};

export default ModalWindow;
