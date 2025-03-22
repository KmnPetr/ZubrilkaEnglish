import './card.css'
import CButton from "../../../../../ui/cButton/CButton.jsx";
import Translation from "./translation/Translation.jsx";
import {useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {setCardUuid} from "../../../../../redux/reducers/phraseReduser.js";

const Card = ({className,card}) =>{
    const dispatch = useDispatch();
    const {idPhrase,indexWord,typeStr,str,language} = useSelector((state) => state.cardEditorReducer);
    const [showDetails, setShowDetails] = useState(false);

    const selectCard=()=>{
        dispatch(setCardUuid(card.uuid, typeStr, idPhrase, indexWord, language))
    }

    return (<div className={`card_box ${className}`}>
        <div className='main_card_info' onClick={()=>setShowDetails(!showDetails)}>
            <p>{card.text}</p>
            <p>{card.transcription}</p>
            <CButton className='useButton' onClick={(event)=>{
                event.stopPropagation();
                selectCard();
            }}>use</CButton>
        </div>

        {showDetails && <Translation card={card}/>}
    </div>)
}
export default Card;