import './card.css'
import CButton from "../../../../../ui/cButton/CButton.jsx";
import Translation from "./translation/Translation.jsx";
import {useDispatch, useSelector} from "react-redux";
import {setCardUuid} from "../../../../../redux/reducers/phraseReduser.js";
import {FaCheck} from "react-icons/fa6";

const Card = ({className,card,usedCardUuid}) =>{
    const dispatch = useDispatch();
    const {idPhrase,indexWord,typeStr,str,language} = useSelector((state) => state.cardEditorReducer);

    const selectCard=()=>{
        dispatch(setCardUuid(card.uuid, typeStr, idPhrase, indexWord, language))
    }

    return (<div className={`card_box ${className}`}>
        <div className='main_card_info'>
            <p>{card.text}</p>
            <p>{card.transcription}</p>
        </div>
        <div>
            <CButton className='useButton' onClick={(event)=>{
                event.stopPropagation();
                selectCard();
            }}>
                <p>use</p>
                {usedCardUuid===card.uuid && <FaCheck style={{color: 'green', width: '15px', height: 'auto'}}/>}
            </CButton>
        </div>

        <Translation card={card}/>
    </div>)
}
export default Card;