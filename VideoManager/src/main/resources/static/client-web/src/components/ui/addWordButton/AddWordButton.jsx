import React from "react";
import { AiOutlinePlusCircle } from "react-icons/ai";
import './addWordButton.css'

/**
 * кнопка для добавления нового слова
 * @param position - значение элементом устанавливающим его обычно это "before" или "after"
 */
const AddWordButton = ({ onClick, position }) => {

    return (
        <div className="add_word_button" onClick={()=>onClick(position)}>
            <AiOutlinePlusCircle />
            <p className="non-selectable">
                add new word
            </p>
        </div>
    );
};

export default AddWordButton;