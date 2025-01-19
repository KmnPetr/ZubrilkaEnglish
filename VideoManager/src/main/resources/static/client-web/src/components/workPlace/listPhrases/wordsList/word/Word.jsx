import { useState } from "react";
import './word.css'
import WordStr from "./wordStr/WordStr";
import AddWordButton from "../../../../ui/addWordButton/AddWordButton";
import { POSITION_AFTER, POSITION_BEFORE } from "../WordsList";

const Word = ({word,index,onChangeWord,addNewWord}) => {
  const [isHover, setIsHover] = useState(false);

  const handleMouseEnter =()=> setIsHover(true)
  const handleMouseLeave =()=> setIsHover(false);
  
  const onChangeStr =(newStr,language)=> {
    const updatedWord = { ...word, [language]: newStr };
    
    onChangeWord(updatedWord,index)
  }

  const addNewWordButtonClick=(position)=>addNewWord(position,index)

  return (
    <div onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave}>
      {isHover && (<AddWordButton onClick={addNewWordButtonClick} position={POSITION_BEFORE}/>)}
      <WordStr str={word.cn} language={'cn'} isHover={isHover} onChangeStr={onChangeStr}/>
      <WordStr str={word.en} language={'en'} isHover={isHover} onChangeStr={onChangeStr}/>
      <WordStr str={word.ru} language={'ru'} isHover={isHover} onChangeStr={onChangeStr}/>
      {isHover && (<AddWordButton onClick={addNewWordButtonClick} position={POSITION_AFTER}/>)}
    </div>
  );
};

export default Word;