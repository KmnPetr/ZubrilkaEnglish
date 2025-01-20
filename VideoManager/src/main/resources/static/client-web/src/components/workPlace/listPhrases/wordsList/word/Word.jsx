import { useState } from "react";
import './word.css'
import WordStr from "./wordStr/WordStr";
import AddWordButton from "../../../../ui/addWordButton/AddWordButton";
import { POSITION_AFTER, POSITION_BEFORE } from "../WordsList";
import { FiTrash2 } from "react-icons/fi";
import SideToolbar from "../../../../ui/sideToolbar/SideToolbar";

const Word = ({word,index,onChangeWord,addNewWord,onDeleteWord}) => {
  const [isHover, setIsHover] = useState(false);

  const handleMouseEnter =()=> setIsHover(true)
  const handleMouseLeave =()=> setIsHover(false);
  
  const onChangeStr =(newStr,language)=> {
    const updatedWord = { ...word, [language]: newStr };
    
    onChangeWord(updatedWord,index)
  }

  const addNewWordButtonClick=(position)=>addNewWord(position,index)

  return (
    <div onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave} className="word-container">
      
      <SideToolbar isShow={isHover} position="top" background="#333">
        <AddWordButton onClick={addNewWordButtonClick} position={POSITION_BEFORE}/>
        <FiTrash2 className='clickable' onClick={()=>onDeleteWord(index)}/>{/**кнопка удаления */}
      </SideToolbar>

      <WordStr str={word.cn} language={'cn'} isHover={isHover} onChangeStr={onChangeStr}/>
      <WordStr str={word.en} language={'en'} isHover={isHover} onChangeStr={onChangeStr}/>
      <WordStr str={word.ru} language={'ru'} isHover={isHover} onChangeStr={onChangeStr}/>
      
      <SideToolbar isShow={isHover} position="bottom" background="#333" z_index={1000}>
        <AddWordButton onClick={addNewWordButtonClick} position={POSITION_AFTER}/>
      </SideToolbar>
    </div>
  );
};

export default Word;