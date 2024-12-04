import React from "react";
import { AiOutlinePlusCircle } from "react-icons/ai";
import {useDispatch} from "react-redux";
import {addNewPhraseToEnd} from "../../../store/reducers/phraseReduser";

/**
 * кнопка имеет больший размер
 * добавляет новую фразу в самый конец списка
 */
const AddNewPhraseAnd = () => {
    const dispatch = useDispatch();
    // Обработчик клика
    const addNewPhrase = () => {
        dispatch(addNewPhraseToEnd());
    };

    return (
        <div className="add-phrase-and-button" onClick={addNewPhrase}>
            <AiOutlinePlusCircle size={40} />
            <p className="non-selectable">
                add new phrase
            </p>
        </div>
    );
};

export default AddNewPhraseAnd;