import './wordStr.css'
import EditableField from "../../../../../ui/editableField/EditableField";
import { useDispatch } from 'react-redux';
import { HiOutlineMicrophone } from "react-icons/hi";
import { openVoiceEditor, WORD } from '../../../../../../redux/reducers/voiceEditorReducer';

const WordStr = ({str,language,isHover,onChangeStr,idPhrase,indexWord}) => {
  const dispatch = useDispatch()
  const onChange =(newStr)=> {
    onChangeStr(newStr,language)
  }
  
  const onClickMicrophone =()=>dispatch(openVoiceEditor({idPhrase,indexWord,str,language,typeStr:WORD}))

  return (
    <div className="str-word">
        {isHover && (<p className="language-p">{language}:</p>)}
        {!str && !isHover && <p style={{ color: '#82aaff', fontSize: '16px', fontFamily: 'Arial' }}>null</p>}
        <div style={{flex:'1',display:'flex',flexDirection:'row'}}>
          <EditableField str={str} onChange={onChange} showMarker={isHover}/>
          <HiOutlineMicrophone className="microphone clickable" onClick={onClickMicrophone}/>
        </div>
    </div>
  );
};

export default WordStr;