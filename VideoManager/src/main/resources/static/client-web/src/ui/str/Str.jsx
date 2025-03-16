import './str.css';
import EditableField from "../editableField/EditableField.jsx";
import {HiOutlineMicrophone} from "react-icons/hi";
import {openVoiceEditor} from "../../redux/reducers/voiceEditorReducer.js";
import {useDispatch} from "react-redux";
import {editStrAction} from "../../redux/reducers/phraseReduser.js";
import PlayVoice, {small} from "../playVoice/PlayVoice.jsx";

const Str =({className, style, str, typeStr, idPhrase, indexWord=null, language, isHover})=>{
    const dispatch = useDispatch();

    const onChange =(newStr)=> {
        dispatch(editStrAction({newStr,typeStr,idPhrase,indexWord,language}))
    }

    const onClickMicrophone =()=>dispatch(openVoiceEditor({idPhrase,indexWord,str:str?.str,language,typeStr}))

    return (
        <div className={`str_box ${className}`} style={style}>
            {isHover && (<p className="language_p">{language}:</p>)}
            {!str && !isHover && <p className='null_str'>null</p>}
            <EditableField str={str?.str} onChange={onChange} showMarker={isHover}/>
            {isHover && <HiOutlineMicrophone className="microphone_str clickable" onClick={onClickMicrophone}/>}
            {isHover && <PlayVoice voiceUuid={str?.voice_uuid} size={small}/>}
        </div>
    )
}
export default Str;