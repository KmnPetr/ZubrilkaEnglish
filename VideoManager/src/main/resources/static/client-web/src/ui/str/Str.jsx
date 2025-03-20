import './str.css';
import EditableField from "../editableField/EditableField.jsx";
import {HiOutlineMicrophone} from "react-icons/hi";
import {openVoiceEditor} from "../../redux/reducers/voiceEditorReducer.js";
import {useDispatch} from "react-redux";
import {editStrAction} from "../../redux/reducers/phraseReduser.js";
import PlayVoice, {small} from "../playVoice/PlayVoice.jsx";
import { FaFileCirclePlus,FaFilePen } from "react-icons/fa6";
import {openCardEditor} from "../../redux/reducers/cardEditorReducer.js";

const Str =({className, style, str, typeStr, idPhrase, indexWord=null, language, isHover,isNativeLang=false})=>{
    const dispatch = useDispatch();

    const onChange =(newStr)=> {
        dispatch(editStrAction({newStr,typeStr,idPhrase,indexWord,language}))
    }

    const onClickMicrophone =()=>dispatch(openVoiceEditor({idPhrase,indexWord,str:str?.str,language,typeStr}))
    const openCardEditor2 =()=>dispatch(openCardEditor({idPhrase,indexWord,str:str?.str,language,typeStr}))

    return (
        <div className={`str_box ${className}`} style={style}>
            {isHover && (<p className="language_p">{language}:</p>)}
            {!str && !isHover && <p className='null_str'>null</p>}
            <EditableField str={str?.str} onChange={onChange} showMarker={isHover}/>
            {isHover && <HiOutlineMicrophone className="microphone_str clickable" onClick={onClickMicrophone}/>}
            {isHover && <PlayVoice voiceUuid={str?.voice_uuid} size={small}/>}
            {!str?.card_uuid && isNativeLang && <FaFileCirclePlus className='clickable' style={{color: '#e4c546'}} onClick={openCardEditor2}/>}
            {str?.card_uuid && isNativeLang && isHover && <FaFilePen className='clickable' onClick={openCardEditor2}/>}
        </div>
    )
}
export default Str;