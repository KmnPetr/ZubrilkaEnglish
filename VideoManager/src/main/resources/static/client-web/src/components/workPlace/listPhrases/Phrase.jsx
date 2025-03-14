import { useState } from "react";
import AddNewPhrase from "./AddNewPhrase";
import { FiTrash2 } from "react-icons/fi";
import DeleteDialog from "./DeleteDialog";
import {useDispatch, useSelector} from "react-redux";
import { removePhraseAction, updateWordsList} from "../../../redux/reducers/phraseReduser";
import PhraseInterval from "./PhraseInterval";
import { RiOpenaiFill } from "react-icons/ri";
import {openAiGptRequest} from "../../../utils/openAiGpt"
import { TbIndentIncrease } from "react-icons/tb";
import {openJsonEditor} from '/src/redux/reducers/jsonEditorReducer.js'
import { PHRASE } from "../../../redux/reducers/jsonEditorReducer";
import WordsList from "./wordsList/WordsList";
import SideToolbar from "../../ui/sideToolbar/SideToolbar";
import Str from "../../ui/str/Str.jsx";

const Phrase =({phrase})=> {
    const dispatch = useDispatch();
    const videoManagement = useSelector(state => state.videoManagementReducer.videoManagement);
    const [isHover, setIsHover] = useState(false);
    const [isDialogVisible, setIsDialogVisible] = useState(false);
    const [isShowWords,setIsShowWords] = useState(false)
    const {native_lang,used_languages} = useSelector(state => state.videoInfoReducer)

    const handleMouseEnter =()=> setIsHover(true)

    const handleMouseLeave =()=> {
        setIsHover(false);
        setIsDialogVisible(false);
    };

    const showDialog =()=> setIsDialogVisible(true)

    const hideDialog =()=> setIsDialogVisible(false)

    const deletePhrase =()=> {
        setIsDialogVisible(false);
        dispatch(removePhraseAction(phrase.id));
    };

    const gptRequest =()=> openAiGptRequest(getNativeStr(),native_lang,used_languages)
    
    const getNativeStr =()=> { return phrase[native_lang].str }

    const setJsonText =()=> {dispatch(openJsonEditor(phrase,PHRASE,native_lang))}

    const onChangeWordsList =(updatedWordsList)=>{
        dispatch(updateWordsList(updatedWordsList,phrase.id));
    }

    return (
        <div style={{position: "relative"}} onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave}>
            
            <SideToolbar isShow={isHover} position="top">
                <div>
                    <AddNewPhrase id={phrase.id} position={"before"}/>
                    <div className="phrase-header">
                        <div>
                            <RiOpenaiFill className='clickable' onClick={gptRequest}/>{/**кнопка перехода на страницу gpt */}
                            <TbIndentIncrease className='clickable' onClick={setJsonText}/>{/**кнопка вставки json текста */}
                        </div>
                        <FiTrash2 className='clickable' onClick={showDialog}/>{/**кнопка удаления */}
                    </div>
                    {(isHover || videoManagement.phraseInterval.idPhrase === phrase.id) && <PhraseInterval phrase={phrase} />}
                </div>
            </SideToolbar>

            <div>
                <div className="phrase">
                    {used_languages&&used_languages.map(lang=>(
                        <Str str={phrase[lang]} typeStr={PHRASE} idPhrase={phrase.id} language={lang} isHover={isHover} key={lang}/>
                    ))}
                    {isShowWords && <WordsList words={phrase.words} onChangeWordsList={onChangeWordsList} idPhrase={phrase.id}/>}
                </div>
            </div>

            {/* диалоговое окно */}
            {isDialogVisible && (<DeleteDialog onDeletePhrase={deletePhrase} onCancel={hideDialog}/>)}

            <SideToolbar isShow={isHover} position="bottom">
                <div>
                    {/* Кнопка со стрелочкой для управления видимостью списка */}
                    <div
                    className="toggle-button"
                    onClick={() => setIsShowWords(!isShowWords)}
                    style={{ cursor: "pointer", display: "flex", alignItems: "center" }}>
                            <span style={{ marginRight: "8px" }}>{isShowWords ? "▲" : "▼"} {/* Стрелочка вниз/вверх */}</span>
                            <span>{isShowWords ? 'hide words' : 'open words'}</span>
                        </div>
                    <AddNewPhrase id={phrase.id} position={"after"}/>
                </div>
            </SideToolbar>
        </div>
    );
};

export default Phrase;