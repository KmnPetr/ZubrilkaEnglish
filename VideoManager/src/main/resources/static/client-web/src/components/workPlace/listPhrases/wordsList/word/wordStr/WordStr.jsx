import React from "react";
import './wordStr.css'
import EditableField from "../../../../../ui/editableField/EditableField";

const WordStr = ({str,language,isHover,onChangeStr}) => {

  const onChange =(newStr)=> {
    onChangeStr(newStr,language)
  }

  return (
    <div className="str-word">
        {isHover && (<p className="language-p">{language}:</p>)}
        {!str && <p style={{ color: '#82aaff', fontSize: '12px', fontFamily: 'Arial' }}>null</p>}
        <div style={{flex:'1'}}>
          <EditableField str={str} onChange={onChange} showMarker={isHover}/>
        </div>
    </div>
  );
};

export default WordStr;