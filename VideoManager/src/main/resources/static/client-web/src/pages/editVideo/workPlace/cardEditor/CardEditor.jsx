import './cardEditor.css';
import {useDispatch, useSelector} from "react-redux";
import ModalWindow from "../../../../ui/modalWindow/ModalWindow.jsx";
import {closeCardEditor} from "../../../../redux/reducers/cardEditorReducer.js";
import StrCardFlipper from "./strCardFlipper/StrCardFlipper.jsx";
import ListАvailableCard from "./listАvailableCard/ListАvailableCard.jsx";

const CardEditor = ()=>{
    const dispatch = useDispatch();
    const {isOpen,idPhrase,indexWord,typeStr,str,language} = useSelector((state) => state.cardEditorReducer);

    const onClose = () => dispatch(closeCardEditor());
    return (
        <ModalWindow isOpen={isOpen} onClose={onClose} width="70%" height="70%">
            <div className="card_editor_box">

                <StrCardFlipper className='str_card_flipper'/>

                <div style={{display:"flex",flexDirection:"row",alignItems:"center"}}>
                    <h1>{"\""+str+"\""}</h1>
                </div>
                <ListАvailableCard className='listAvailableCard' str={str}/>
            </div>
        </ModalWindow>
    )
}
export default CardEditor;