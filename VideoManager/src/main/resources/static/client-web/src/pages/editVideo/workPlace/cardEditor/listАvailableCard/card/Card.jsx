import './card.css'
import CButton from "../../../../../../ui/cButton/CButton.jsx";
import Transcription from "./transcription/Transcription.jsx";
import Translation from "./translation/Translation.jsx";

const Card = ({className,card}) =>{
    return (<div className={`card_box ${className}`} onClick={()=>console.log("card clicked")}>
        <p>{card.text}</p>
        <CButton className='useButton' onClick={(event)=>{
            event.stopPropagation();
            console.log('ButtonClick');
        }}>use</CButton>

        <Transcription card={card}/>
        <Translation card={card}/>
    </div>)
}
export default Card;