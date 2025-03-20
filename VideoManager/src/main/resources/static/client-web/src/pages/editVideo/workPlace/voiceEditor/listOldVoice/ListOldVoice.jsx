import './listOldVoice.css';
import {useEffect, useState} from "react";
import {useSelector} from "react-redux";
import {getListSimilarVoices} from "../../../../../api/voiceService.js";
import {SlUser, SlUserFemale} from "react-icons/sl";
import Rating from "@mui/material/Rating";
import PlayVoice, {small} from "../../../../../ui/playVoice/PlayVoice.jsx";
import CButton from "../../../../../ui/cButton/CButton.jsx";
import { FaCheck } from "react-icons/fa6";

const ListOldVoice=({className,style,str, onSelectOldVoice,currentVoiceUuid})=>{
    const {rating_voices} = useSelector(state=>state.authReducer.user)
    const {uuid} = useSelector(state => state.translationReducer.translation)
    const [similarVoices, setSimilarVoices] = useState([])

    useEffect(()=>{
        getListSimilarVoices({ text:str, translation_uuid: uuid})
            .then(listVoice=>{
                console.log('listVoice=',JSON.stringify(listVoice,null, 2))
                setSimilarVoices(listVoice);
            })
            .catch(err=>{setSimilarVoices([])})
    },[str])

    return (
        <div className={`listOldVoice ${className}`} style={style}>
        {similarVoices && similarVoices.length > 0 && (
            similarVoices.map(voice=>(
                <div className='old_voice' key={voice.uuid}>
                    <p>{voice.text}</p>
                    <PlayVoice voiceUuid={voice.uuid} size={small} style={{margin:'0px'}}/>
                    <CButton onClick={()=>onSelectOldVoice(voice.uuid)} className='select_old_V_button'>
                        <p>use</p>
                        {currentVoiceUuid===voice.uuid && <FaCheck style={{color: 'green', width: '15px', height: 'auto'}}/>}
                    </CButton>
                    {voice.sex==='male'&&<SlUser className='male_icon'/>}
                    {voice.sex==='female'&&<SlUserFemale className='female_icon'/>}
                    <p>{voice.voice}</p>
                    {rating_voices[voice.voice]&& rating_voices[voice.voice] > 0 &&
                        <Rating
                            disabled
                            value={rating_voices[voice.voice] || null}
                            max={3}
                            size='small'/>
                    }
                </div>
            ))
        )}
    </div>)
}
export default ListOldVoice;