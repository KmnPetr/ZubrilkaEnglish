import './usedCard.css';
import {useEffect, useState} from "react";
import {addNewTranslation, getCardByUuid} from "../../../../../api/cardService.js";
import {useDispatch, useSelector} from "react-redux";
import {editStrAction, editTranscriptionAction} from "../../../../../redux/reducers/phraseReduser.js";

//используемая на данный момент для строки карта
const UsedCard=({className,style,card_uuid,strObj,typeStr,idPhrase,indexWord})=>{
    const dispatch = useDispatch();
    const [card,setCard]=useState(null);
    const {native_lang,used_languages} = useSelector(state => state.videoInfoReducer)

    useEffect(()=>{
        getCardFromServer()
    },[card_uuid])
    const getCardFromServer=()=>{
        if(card_uuid){
            getCardByUuid(card_uuid)
                .then(card=>setCard(card))
                .catch(e=>console.error(e))
        } else setCard(null)
    }

    const applyTranscription=()=>{
        dispatch(editTranscriptionAction({newTranscription:card?.transcription,typeStr,idPhrase,indexWord,language:native_lang}))
    }
    const applyTranslation=(lang,translation)=>{
        dispatch(editStrAction({newStr:translation,typeStr,idPhrase,indexWord,language:lang}))
    }
    //переводы строки которые не совпадают с карточкой будут предложены для добавления в существующую карточку
    const listMismatchedTranslations = () => {
        const strObjListTransl = used_languages
            .filter(lang => lang !== native_lang)
            .map(lang => ({ str: strObj[lang]?.str, lang: lang }))
            .filter(obj => Boolean(obj.str));

        const usedCardListTransl = used_languages
            .filter(lang => lang !== native_lang)
            .flatMap(lang => card.translation[lang] || [])
            .filter(Boolean);

        // Находим несовпадающие строки
        const mismatched = strObjListTransl.filter(strDto =>
            !usedCardListTransl.includes(strDto.str)
        );

        return mismatched;
    };

    //отправит на сервер запрос на добавление нового перевода к текущей используемой карточке
    const addNewTranslation_2=(strDto)=>{
        addNewTranslation({...strDto, card_uuid})
            .then((res)=>setCard(res))
            .catch(e=>console.error(e))
    }

    return(<div className={`usedCard ${className}`}>

        <p style={{fontStyle:'italic'}}>Used Card</p>
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
                { listMismatchedTranslations().length > 0 &&
                    <div>
                        <div className='line_u_card'></div>
                        {listMismatchedTranslations().map((strDto, i) => (
                            <div key={i} className='add_new_transl'>
                                <p>{strDto.str}</p>
                                <p className='apply_text clickable no_select' onClick={()=>addNewTranslation_2(strDto)}>add new translation</p>
                            </div>))}
                    </div>
                }
            </div>
        )}
    </div>)
}
export default UsedCard;
