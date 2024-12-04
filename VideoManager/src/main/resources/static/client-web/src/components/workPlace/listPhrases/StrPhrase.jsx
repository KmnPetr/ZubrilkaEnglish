import React, {useState} from "react";
import { TfiMarkerAlt } from "react-icons/tfi";
import {FiCheck, FiX} from "react-icons/fi";
import {useDispatch} from "react-redux";
import {editStrAction} from "../../../store/reducers/phraseReduser";

/**
 * компонент отвечает за отображение и редактирование строки фразы каждого языка поотдельности
 * каждая StrPhrase отвечает за свой язык например за китайский
 */
const StrPhrase = ({str,idPhrase,language, isHover}) => {
    const dispatch = useDispatch();
    const [isEditable, setIsEditable] = useState(false);
    const [inputValue, setInputValue] = useState(str); // Строка, которая будет отображаться и изменяться

    const clickEdit = () => {
        setIsEditable(true);
    };
    const handleInputChange = (e) => {
        setInputValue(e.target.value);
    }

    const onCancelEdit = () => {
        setIsEditable(false);
        setInputValue(str)
    }
    const onApplyChanges = () => {
        dispatch(editStrAction(idPhrase,language,inputValue))
        setIsEditable(false)
    }

    return (
        <div>
            {isEditable ? (
                <div className="str-phrase">
                    <input type="text" value={inputValue} onChange={handleInputChange}/>
                    <FiX onClick={onCancelEdit} style={{marginRight:'10px'}} className='clickable'/>
                    <FiCheck onClick={onApplyChanges} className='clickable'/>
                </div>
            ) : (
                isHover ? (
                    <div className="str-phrase">
                        <p>{str}</p>
                        <TfiMarkerAlt onClick={clickEdit} className="clickable"/>
                    </div>
                ) : (
                    <div className="str-phrase">
                        <p>{str}</p>
                    </div>
                )
            )}
        </div>
    );
};

export default StrPhrase;