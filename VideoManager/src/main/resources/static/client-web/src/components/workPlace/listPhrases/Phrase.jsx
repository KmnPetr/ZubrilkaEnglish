import { useState } from "react";
import AddNewPhrase from "./AddNewPhrase";
import { FiTrash2 } from "react-icons/fi";
import DeleteDialog from "./DeleteDialog";
import {useDispatch, useSelector} from "react-redux";
import {CN, EN, removePhraseAction, RU} from "../../../store/reducers/phraseReduser";
import StrPhrase from "./StrPhrase";
import PhraseInterval from "./PhraseInterval";
import { RiOpenaiFill } from "react-icons/ri";
import {openAiGptRequest} from "../../../utils/openAiGpt"
import { TbIndentIncrease } from "react-icons/tb";
import {openJsonEditor} from '/src/store/reducers/jsonEditorReducer.js'

const Phrase =({phrase})=> {
    const dispatch = useDispatch();
    const videoManagement = useSelector(state => state.videoManagementReducer.videoManagement);
    const [isHover, setIsHover] = useState(false);
    const [isDialogVisible, setIsDialogVisible] = useState(false);
    const nativeLang = `cnStr`

    const handleMouseEnter =()=> {
        setIsHover(true);
    };

    const handleMouseLeave =()=> {
        setIsHover(false);
        setIsDialogVisible(false);
    };

    const showDialog =()=> {
        setIsDialogVisible(true);
    };

    const hideDialog =()=> {
        setIsDialogVisible(false);
    };
    const deletePhrase =()=> {
        setIsDialogVisible(false);
        dispatch(removePhraseAction(phrase.id));
    };

    const gptRequest =()=> {
        openAiGptRequest(getNativeStr(),CN)
    }
    const getNativeStr =()=> {
        return phrase[nativeLang]
    }

    const setJsonText =()=> {dispatch(openJsonEditor())}

    return (
        <div style={{position: "relative"}} onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave}>
            <div>
                {isHover && <AddNewPhrase id={phrase.id} position={"before"}/>}
                <div className="phrase">
                    {(isHover || videoManagement.phraseInterval.idPhrase === phrase.id) && <PhraseInterval phrase={phrase} />}
                    {isHover && 
                        <div className="phrase-header">
                            <div>
                                <p style={{fontSize:'10px'}}>Id: {phrase.id}</p>
                                <RiOpenaiFill className='clickable' onClick={gptRequest}/>{/**кнопка перехода на страницу gpt */}
                                <TbIndentIncrease className='clickable' onClick={setJsonText}/>{/**кнопка вставки json текста */}
                            </div>
                            <FiTrash2 className='clickable' onClick={showDialog}/>{/**кнопка удаления */}
                        </div>
                    }
                    <StrPhrase str={phrase.cnStr} idPhrase={phrase.id} language={CN} isHover={isHover}/>
                    <StrPhrase str={phrase.enStr} idPhrase={phrase.id} language={EN} isHover={isHover}/>
                    <StrPhrase str={phrase.ruStr} idPhrase={phrase.id} language={RU} isHover={isHover}/>
                </div>
                {isHover && <AddNewPhrase id={phrase.id} position={"after"}/>}
            </div>

            {/* диалоговое окно */}
            {isDialogVisible && (<DeleteDialog onDeletePhrase={deletePhrase} onCancel={hideDialog}/>)}
        </div>
    );
};

export default Phrase;