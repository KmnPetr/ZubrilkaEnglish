import React from "react";
import { AiOutlinePlusCircle } from "react-icons/ai";
import {useDispatch} from "react-redux";
import {addNewPhrase} from "../../../store/reducers/phraseReduser";

/**
 * кнопка для добавления новой фразы
 * @param index - индекс элемента рядом с которым надо добавить новую фразу
 * @param position - значение "before" до указанной фразы в списке, значение "after" - после
 */
const AddNewPhrase = ({ id, position }) => {
    const dispatch = useDispatch();
    // Обработчик клика
    const handleAddNewPhrase = () => {
        dispatch(addNewPhrase(id, position));
    };

    return (
        <div className="add-phrase-button" onClick={handleAddNewPhrase}>
            <AiOutlinePlusCircle />
            <p className="non-selectable">
                add new phrase
            </p>
        </div>
    );
};

export default AddNewPhrase;