import React, { useState } from "react";
import './wordsList.css';
import Word from "./word/Word";

/**
 * Список слов/фраз с возможностью скрывать/показывать
 */
const WordsList = ({ words,isPhraseHover }) => {
  const [isExpanded, setIsExpanded] = useState(false); // Состояние для управления видимостью списка

    if(!words) return null;

  return (
    <div>
        {/* Кнопка со стрелочкой для управления видимостью списка */}
        {isPhraseHover && (
            <div
                className="toggle-button"
                onClick={() => setIsExpanded(!isExpanded)}
                style={{ cursor: "pointer", display: "flex", alignItems: "center" }}
            >
                <span style={{ marginRight: "8px" }}>
                {isExpanded ? "▼" : "▲"} {/* Стрелочка вниз/вверх */}
                </span>
                <span>list of words</span>
            </div>
        )}
        <div className="wordsList">
            {/* Отображаем список только если isExpanded равно true */}
            {isExpanded && (
                <ul>
                {words.map((word, index) => (
                    <li key={index} style={{ marginBottom: "10px" }}>
                    <Word word={word.cn} language={'cn'} index={index} />
                    <Word word={word.en} language={'en'} index={index} />
                    <Word word={word.ru} language={'ru'} index={index} />
                    </li>
                ))}
                </ul>
            )}
        </div>
    </div>
  );
};

export default WordsList;
