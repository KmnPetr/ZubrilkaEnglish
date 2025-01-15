import React, {useState} from "react";
import { TfiMarkerAlt } from "react-icons/tfi";
import {FiCheck, FiX} from "react-icons/fi";
import "./VideoInfo.css"

/**
 * in this component, one of the video information fields is edited
 */
const EditInfo = ({fieldName,fieldNameAlias,fieldValue,onUpdateVideoInfoField}) => {
    const [isEditable, setIsEditable] = useState(false);
    const [inputValue, setInputValue] = useState(fieldValue);

    const clickEdit = () => {
        setIsEditable(true);
    };
    const handleInputChange = (e) => {
        setInputValue(e.target.value);
    }

    const onCancelEdit = () => {
        setIsEditable(false);
        setInputValue(fieldValue)
    }
    const onApplyChanges = () => {
        onUpdateVideoInfoField(fieldName,inputValue)
        setIsEditable(false)
    }
    const handleKeyDown = (e) => {
        if (e.key === 'Enter') {
            onApplyChanges();
        }
    }

    return (
        <div>
            {isEditable ? (
                <div className="edited-field">
                    <p className="fieldNameAlias">{fieldNameAlias}:  </p>
                    <input 
                        type="text"
                        value={inputValue}
                        onChange={handleInputChange}
                        onKeyDown={handleKeyDown}/>
                    <FiX onClick={onCancelEdit} style={{marginRight:'10px'}} className='clickable'/>
                    <FiCheck onClick={onApplyChanges} className='clickable'/>
                </div>
            ) : (
                <div className="edited-field">
                    <p className="fieldNameAlias">{fieldNameAlias}:</p>
                    <p style={{marginRight:'8px'}}>{fieldValue}</p>
                    <TfiMarkerAlt onClick={clickEdit} className="clickable"/>
                </div>
            )}
        </div>
    );
};

export default EditInfo;