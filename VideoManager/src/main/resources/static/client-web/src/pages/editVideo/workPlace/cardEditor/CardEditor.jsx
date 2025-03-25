import './cardEditor.css';
import {useDispatch, useSelector} from "react-redux";
import ModalWindow from "../../../../ui/modalWindow/ModalWindow.jsx";
import {closeCardEditor} from "../../../../redux/reducers/cardEditorReducer.js";
import StrCardFlipper from "./strCardFlipper/StrCardFlipper.jsx";
import StrAndTransl from "./strAndTransl/StrAndTransl.jsx";
import {useEffect, useState} from "react";
import {PHRASE, setCardUuid, WORD} from "../../../../redux/reducers/phraseReduser.js";
import UsedCard from "./usedCard/UsedCard.jsx";
import {createNewCard, getListCards} from "../../../../api/cardService.js";
import Card from "./card/Card.jsx";
import CButton from "../../../../ui/cButton/CButton.jsx";

const CardEditor = ()=>{
    const dispatch = useDispatch();
    const {isOpen,idPhrase,indexWord,typeStr,str,language} = useSelector((state) => state.cardEditorReducer);
    const phrases = useSelector(state => state.phraseReducer.phrases);
    const [strObj,setStrObj]=useState(null)
    const [card_uuid,setCard_uuid]=useState(null)
    const [listCards, setListCards] = useState([])
    const {native_lang,used_languages} = useSelector(state => state.videoInfoReducer)
    const [errors,setErrors]=useState([])

    useEffect(()=>{
        updateListCards()
    },[str])

    //запросит с сервера список карт похожих по строке на str
    const updateListCards=()=>{
        getListCards({ text:str})
            .then(listVoice=>{
                setListCards(listVoice);
            })
            .catch(err=>{setListCards([])})
    }

    //найдет strObj среди списка фраз
    //strObj может быть как фраза так и Word они похожи по набору переводов на другие языки
    useEffect(()=>{
        let strObj_new={}
        if(idPhrase && idPhrase){
            if(typeStr===PHRASE)
                strObj_new = phrases.find(phrase=>phrase.id === idPhrase)
            if(typeStr===WORD){
                const phrase=phrases.find(phrase=>phrase.id === idPhrase)
                strObj_new = phrase.words[indexWord]
            }
        }
        setStrObj(strObj_new)
        setCard_uuid(strObj_new[language]?.card_uuid)
    },[idPhrase,indexWord,typeStr,phrases])

    const onClose = () => dispatch(closeCardEditor());
    const clearLink=()=>dispatch(setCardUuid(null, typeStr, idPhrase, indexWord, language))

    //запрос на создание новой карточки
    //перед созданием новой требуется ее обеспечить некоторыми полями из strObj иначе выдаем ошибки валидации
    const createNewCard_2 = () => {
        const newCard = {
            uuid: null,
            text: strObj[native_lang]?.str,
            transcription: strObj[native_lang]?.transcription,
            translation: used_languages.reduce((acc, lang) => {
                if (lang !== native_lang) {
                    acc[lang] = [strObj[lang]?.str];
                }
                return acc;
            }, {}),
            language: native_lang,
            level: null,
            voice_uuid: strObj[native_lang].voice_uuid,
        };
        console.log(JSON.stringify(newCard, null, 2));

        const errors2 = []
        if(!newCard.text || !newCard.text.trim()) errors2.push(`The line "${native_lang.toUpperCase()}" must not be empty.`)
        if(!newCard.transcription || !newCard.transcription.trim()) errors2.push(`Transcription must not be empty.`)
        used_languages.forEach(lang=>{
            if(native_lang!==lang){
                const str = newCard.translation[lang][0]
                if(!str || !str.trim()) errors2.push(`The line "${lang.toUpperCase()}" must not be empty.`)
            }
        })
        if (!newCard.voice_uuid) errors2.push(`Voice of line "${native_lang.toUpperCase()}" must not be empty.`)

        if (errors2.length > 0) {
            setErrors(errors2);
            setTimeout(() => {
                setErrors(prevErrors =>
                    prevErrors === errors2 ? [] : prevErrors
                );
            }, 3000);}
        else {
            setErrors([])
            createNewCard(newCard)
                .then((uuidNewCard) => {
                    dispatch(setCardUuid(uuidNewCard, typeStr, idPhrase, indexWord, language))
                    updateListCards()
                })
                .catch(err=>{console.error(err)})
        }

    };

    return (
        <ModalWindow isOpen={isOpen} onClose={onClose} width="70%" height="70%">
            <div className="card_editor_box">

                <StrCardFlipper className='str_card_flipper'/>

                <div style={{display:"flex",flexDirection:"column",alignItems:"center",margin:'1rem'}}>
                    {strObj &&
                        <StrAndTransl
                            strObj={strObj}
                            typeStr={typeStr}
                            idPhrase={idPhrase}
                            indexWord={indexWord}/>}

                    <div style={{display:"flex",flexDirection:"row",alignItems:"center"}}>
                        {!card_uuid && <p style={{ color: "#e8e33f" }}>There is no link to the card.</p>}

                        {!card_uuid && <CButton className='c_new_card_button' onClick={createNewCard_2}>Create new card</CButton>}
                        {card_uuid && <CButton className='clear_link_button' onClick={clearLink}>Clear link</CButton>}
                    </div>
                    <div className='errors_card'>
                        {errors && errors.map((e,i)=>(<p key={i}>{e}</p>))}
                    </div>

                    {card_uuid &&
                        <UsedCard
                            card_uuid={card_uuid}
                            strObj={strObj}
                            typeStr={typeStr}
                            idPhrase={idPhrase}
                            indexWord={indexWord}/>}

                </div>

                <div className='listAvailableCard'>
                    <p style={{fontStyle:'italic',margin:'0.5rem'}}>List of available cards</p>
                    {listCards && listCards.map(card => (
                        <Card key={card.uuid} card={card} usedCardUuid={card_uuid}/>
                    ))}

                </div>
            </div>
        </ModalWindow>
    )
}
export default CardEditor;