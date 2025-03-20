import './castomVoiceSelector.css';
import {useEffect, useRef, useState} from "react";
import { IoIosArrowDown,IoIosArrowUp } from "react-icons/io";
import { SlUser,SlUserFemale } from "react-icons/sl";
import Rating from '@mui/material/Rating';

/**
 * кастомный селектор потому-что стандартные селекторы не позволяют например вставлять рейтинг по звездочкам и обрабатывать нормально их клики
 */
const CastomVoiceSelector=({className, style,listVoice,selectedVoice,onSelectVoice,onClickRating,ratingVoices})=>{

    const [isOpenList, setIsOpenList] = useState(false);
    const listRef = useRef(null);

    //следит за сокрытием списка при нажатии на область вне его
    useEffect(() => {
        function handleClose(event) {
            // Закрываем, если клик был вне списка
            if (listRef.current && !listRef.current.contains(event.target)) {
                setIsOpenList(false);
            }
        }
        // Слушаем клики и прокрутку
        document.addEventListener("mousedown", handleClose);
        return () => {
            document.removeEventListener("mousedown", handleClose);
        };
    }, []);

    const onSelectVoice_2=(voice)=>{
        setIsOpenList(false);
        onSelectVoice(voice)
    }

    return (<div className={`castom_voice_selector_box ${className}`} style={style}>
        <div className='selectedVoice'>
            {selectedVoice && <VoiceElement
                voiceObj={selectedVoice}
                onClickVoice={()=>{}}
                rating={ratingVoices[selectedVoice.voice]}
                onClickRating={onClickRating}/>}

            {isOpenList && <div ref={listRef} className='listVoice'>
                {listVoice && listVoice.map((voiceObj,idx)=>
                    <VoiceElement
                        voiceObj={voiceObj}
                        key={idx}
                        onClickVoice={onSelectVoice_2}
                        rating={ratingVoices[voiceObj.voice]}
                        onClickRating={onClickRating}/>
                )}
            </div>}
        </div>
        <div className='arrow_box'>
            {isOpenList && <IoIosArrowUp className='arrow_c_ls clickable' onClick={()=>setIsOpenList(false)}/>}
            {!isOpenList && <IoIosArrowDown className='arrow_c_ls clickable' onClick={()=>setIsOpenList(true)}/>}
        </div>
    </div>);
}
export default CastomVoiceSelector;

const VoiceElement=({voiceObj,onClickVoice,rating,onClickRating})=>{

    return(
      <div className='voice_element' onClick={()=>onClickVoice({voice:voiceObj.voice,sex:voiceObj.sex})}>
          {voiceObj.sex==='male'&&<SlUser className='male_icon'/>}
          {voiceObj.sex==='female'&&<SlUserFemale className='female_icon'/>}
          <p className='voice_name'>{voiceObj.voice}</p>
          {voiceObj.pro === '1' && <p className='pro'>pro</p>}
          <Rating
              onClick={(e)=>e.stopPropagation()}
              value={rating || null}
              max={3}
              size='small'
              onChange={(event, newValue) => onClickRating(event, newValue,voiceObj.voice)}/>
      </div>
    );
}