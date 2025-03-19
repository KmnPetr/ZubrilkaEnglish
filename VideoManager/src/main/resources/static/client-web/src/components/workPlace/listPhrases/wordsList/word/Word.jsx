import { useState } from "react";
import './word.css'
import AddWordButton from "../../../../../ui/addWordButton/AddWordButton";
import { POSITION_AFTER, POSITION_BEFORE } from "../WordsList";
import { FiTrash2 } from "react-icons/fi";
import SideToolbar from "../../../../../ui/sideToolbar/SideToolbar";
import {useSelector} from "react-redux";
import Str from "../../../../../ui/str/Str.jsx";
import {WORD} from "../../../../../redux/reducers/phraseReduser.js";

const Word = ({word,index,addNewWord,onDeleteWord,idPhrase}) => {
  const [isHover, setIsHover] = useState(false);
    const {native_lang,used_languages} = useSelector(state => state.videoInfoReducer)

  const handleMouseEnter =()=> setIsHover(true)
  const handleMouseLeave =()=> setIsHover(false);

  const addNewWordButtonClick=(position)=>addNewWord(position,index)

  return (
    <div onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave} className="word-container">
      
      <SideToolbar isShow={isHover} position="top" background="#333">
        <AddWordButton onClick={addNewWordButtonClick} position={POSITION_BEFORE}/>
        <FiTrash2 className='clickable' onClick={()=>onDeleteWord(index)}/>{/**кнопка удаления */}
      </SideToolbar>

        {used_languages&&used_languages.map(lang=>(
            <Str
                str={word[lang]}
                typeStr={WORD}
                idPhrase={idPhrase}
                indexWord={index}
                language={lang}
                isHover={isHover}
                key={lang}
                isNativeLang={lang===native_lang}/>
        ))}
      
      <SideToolbar isShow={isHover} position="bottom" background="#333" z_index={1000}>
        <AddWordButton onClick={addNewWordButtonClick} position={POSITION_AFTER}/>
      </SideToolbar>
    </div>
  );
};

export default Word;