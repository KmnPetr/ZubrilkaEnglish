import React from "react";
import './word.css'

const Word = ({word,language,index}) => {


  return (
    <div className="str-word">
        <p className="language-p">{language}:</p>
        <p>{word}</p>
    </div>
  );
};

export default Word;