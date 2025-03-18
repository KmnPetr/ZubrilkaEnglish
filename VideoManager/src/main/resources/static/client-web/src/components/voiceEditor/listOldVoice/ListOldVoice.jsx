import './listOldVoice.css';
import {useEffect, useState} from "react";
import {useSelector} from "react-redux";
import {getListSimilarVoices} from "../../../api/voiceService.js";
import {SlUser, SlUserFemale} from "react-icons/sl";
import Rating from "@mui/material/Rating";

const ListOldVoice=({className,style,str, onSelectOldVoice})=>{
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

    return (<div className={`listOldVoice ${className}`} style={style}>

        {similarVoices && similarVoices.length > 0 && (
            similarVoices.map(voice=>(
                <div className='old_voice' key={voice.uuid}>
                    <p>{voice.text}</p>
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