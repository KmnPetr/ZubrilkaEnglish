import './usedCard.css';
import {useEffect, useState} from "react";
import {getCardByUuid} from "../../../../../api/cardService.js";
import {useDispatch, useSelector} from "react-redux";
import {editStrAction} from "../../../../../redux/reducers/phraseReduser.js";

//используемая на данный момент для строки карта
const UsedCard=({className,style,card_uuid,strObj,typeStr,idPhrase,indexWord})=>{
    const dispatch = useDispatch();
    const [card,setCard]=useState(null);
    const {native_lang,used_languages} = useSelector(state => state.videoInfoReducer)

    useEffect(()=>{
        if(card_uuid){
            getCardByUuid(card_uuid)
                .then(card=>setCard(card))
                .catch(e=>console.error(e))
        } else setCard(null)
    },[card_uuid])

    const applyTranscription=()=>{
        console.log('new transcription='+card.transcription)
    }
    const applyTranslation=(lang,translation)=>{
        dispatch(editStrAction({newStr:translation,typeStr,idPhrase,indexWord,language:lang}))
    }
    return(<div className={`usedCard ${className}`}>

        <p>Used Card</p>
        {card && native_lang && (
            <div>
                <div className='field_card'>
                    <p className='field_card_name'>{native_lang+':'}</p>
                    <p>{card.text}</p>
                </div>
                <div className='field_card'>
                    <p className='field_card_name'>transcription:</p>
                    <p>{card.transcription}</p>
                    {card.transcription!==strObj[native_lang]?.transcription &&
                        <p className='apply_text clickable no_select' onClick={applyTranscription}>apply</p>}
                </div>
                {used_languages &&
                    used_languages
                        .filter(lang => lang !== native_lang)
                        .flatMap(lang =>
                            (card.translation[lang] || []).map((transl, i) => (
                                <div className='field_card' key={lang+i}>
                                    <p className='field_card_name'>{lang+':'}</p>
                                    <p>{transl}</p>
                                    {card.translation!==strObj[lang]?.str &&
                                        <p className='apply_text clickable no_select' onClick={()=>applyTranslation(lang,transl)}>apply</p>}
                                </div>
                            ))
                        )}
            </div>
        )}
    </div>)
}
export default UsedCard;
