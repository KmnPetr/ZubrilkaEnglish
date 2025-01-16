import React from 'react';
import { FiX } from 'react-icons/fi';
import './ModalWindow.css';

const ModalWindow = ({ isOpen, onClose, children }) => {
  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-dialog" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <FiX onClick={onClose} className="close-icon clickable" />
        </div>
        <div className="modal-content">
          {children}
        </div>
      </div>
    </div>
  );
};

export default ModalWindow;