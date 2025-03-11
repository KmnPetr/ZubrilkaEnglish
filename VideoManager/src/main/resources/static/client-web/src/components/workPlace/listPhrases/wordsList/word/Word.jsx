import { useState } from "react";
import './word.css'
import WordStr from "./wordStr/WordStr";
import AddWordButton from "../../../../ui/addWordButton/AddWordButton";
import { POSITION_AFTER, POSITION_BEFORE } from "../WordsList";
import { FiTrash2 } from "react-icons/fi";
import SideToolbar from "../../../../ui/sideToolbar/SideToolbar";
import {useSelector} from "react-redux";

const Word = ({word,index,onChangeWord,addNewWord,onDeleteWord,idPhrase}) => {
  const [isHover, setIsHover] = useState(false);
    const {native_lang,used_languages} = useSelector(state => state.videoInfoReducer)

  const handleMouseEnter =()=> setIsHover(true)
  const handleMouseLeave =()=> setIsHover(false);
  
  const onChangeStr =(newStr,language)=> {
    const updatedWord = { ...word, [language]: {...word[language],str:newStr} };
    
    onChangeWord(updatedWord,index)
  }

  const addNewWordButtonClick=(position)=>addNewWord(position,index)

  return (
    <div onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave} className="word-container">
      
      <SideToolbar isShow={isHover} position="top" background="#333">
        <AddWordButton onClick={addNewWordButtonClick} position={POSITION_BEFORE}/>
        <FiTrash2 className='clickable' onClick={()=>onDeleteWord(index)}/>{/**кнопка удаления */}
      </SideToolbar>

        {used_languages&&used_languages.map(lang=>(
            <WordStr str={word[lang]?.str} language={lang} isHover={isHover} onChangeStr={onChangeStr} idPhrase={idPhrase} indexWord={index} key={lang}/>
        ))}
      
      <SideToolbar isShow={isHover} position="bottom" background="#333" z_index={1000}>
        <AddWordButton onClick={addNewWordButtonClick} position={POSITION_AFTER}/>
      </SideToolbar>
    </div>
  );
};

export default Word;