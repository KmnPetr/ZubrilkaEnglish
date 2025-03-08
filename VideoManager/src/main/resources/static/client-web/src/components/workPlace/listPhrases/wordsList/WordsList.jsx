import React, { useState } from "react";
import './wordsList.css';
import Word from "./word/Word";
import AddWordButton from "../../../ui/addWordButton/AddWordButton";

export const POSITION_AFTER = 'POSITION_AFTER'
export const POSITION_BEFORE = 'POSITION_BEFORE'
export const POSITION_ONLY_ONE = 'POSITION_ONLY_ONE'
export const POSITION_END = 'POSITION_END'

/**
 * Список слов/фраз с возможностью скрывать/показывать
 */
const WordsList = ({ words,onChangeWordsList,idPhrase }) => {

  const onChangeWord = (updatedWord, index) => {
    const updatedWordsList = words.map((word, i) => 
        i === index ? updatedWord : word
    );
    
    onChangeWordsList(updatedWordsList);
};
    const addNewWord = (position, index) => {
        const updatedWordsList = (() => {
            switch (position) {
                case POSITION_BEFORE: {
                    const newWords = [...words]; // Копируем исходный массив
                    newWords.splice(index, 0, {}); // Вставляем пустой объект перед элементом с указанным индексом
                    return newWords;
                }
                case POSITION_AFTER: {
                    const newWords = [...words]; // Копируем исходный массив
                    newWords.splice(index + 1, 0, {}); // Вставляем пустой объект после элемента с указанным индексом
                    return newWords;
                }
                case POSITION_END:
                    return [...words, {}]; // Добавляем пустой объект в конец
                case POSITION_ONLY_ONE:
                    return [{}]; // Возвращаем массив только с одним пустым объектом
                default:
                    console.error(`Invalid Position: ${position}`);
                    return words;
            }
        })(); // Немедленно вызываемая функция возвращает обновленный массив

    
        onChangeWordsList(updatedWordsList);
    };
    
    const onDeleteWord = (index) => {
        const updatedWordsList = words.filter((_, i) => i !== index);
        onChangeWordsList(updatedWordsList);
    };

    return (
        <div>
            <div className="wordsList">
                {/* Отображаем список только если isExpanded равно true */}
                {words && (
                    <ul>
                    {words.map((word, index) => (
                        <li key={index} style={{ marginBottom: "10px" }}>
                            <Word word={word} index={index} onChangeWord={onChangeWord} addNewWord={addNewWord} onDeleteWord={onDeleteWord} idPhrase={idPhrase}/>
                        </li>
                    ))}
                    </ul>
                )}
                {words && words.length === 0 && <AddWordButton onClick={addNewWord} position={POSITION_END}/> }
                {!words && <AddWordButton onClick={addNewWord} position={POSITION_ONLY_ONE}/>}
            </div>
        </div>
    );
};

export default WordsList;
