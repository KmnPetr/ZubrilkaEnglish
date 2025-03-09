import React from 'react';
import Editor from 'react-simple-code-editor';
import Prism from 'prismjs';
import 'prismjs/themes/prism-okaidia.css'; // Тема подсветки синтаксиса
import 'prismjs/components/prism-json'; // Поддержка JSON
import ModalWindow from '../ui/ModalWindow';
import { useDispatch, useSelector } from 'react-redux';
import { closeJsonEditor, updateJsonText } from '/src/redux/reducers/jsonEditorReducer.js';
import './JsonEditor.css';
import CButton from '../ui/CButton';
import { PHRASE } from '../../redux/reducers/jsonEditorReducer';
import { updatePhrase } from '../../redux/reducers/phraseReduser';

const JsonEditor = () => {
  const dispatch = useDispatch();
  const { isOpen,jsonObject, editableJson,typeObject,nativeLang } = useSelector((state) => state.jsonEditorReducer);

  const highlight = (jsonText) => Prism.highlight(jsonText, Prism.languages.json, 'json');

  const onClose = () => dispatch(closeJsonEditor()); // Закрывает окно
  const onCodeChange = (editableJson) => dispatch(updateJsonText(editableJson)); // Сохраняет изменения

  const apply =()=>{
    try {
      switch (typeObject) {
        case PHRASE:
          dispatch(updatePhrase(convertJsonToPhrase(jsonObject,editableJson,nativeLang)))
          onClose()
          break;
        default:console.error("Invalid typeObject")
      }
    } catch (error) {
      console.error('Invalid JSON string:', error.message);
    }
  }

  return (
    <ModalWindow isOpen={isOpen} onClose={onClose} width="70%" height="70%">
      <div className="json-editor-container">
        <div className="button-container">
          <CButton text={'Apply'} onClick={apply}/>
        </div>
        <div className="json-editor-wrapper">
          <Editor
            value={editableJson}
            onValueChange={onCodeChange}
            highlight={highlight}
            className="json-editor-textarea"
          />
        </div>
      </div>
    </ModalWindow>
  );
};

export default JsonEditor;

//перебираем обьект редактированный фильтруем ключи чтобы лишние ключи не попали в обьект
const convertJsonToPhrase = (jsonObject, editableJson,nativeLang) => {
  let updatedPhrase = jsonObject;
  const editabledObject = JSON.parse(editableJson);

  
  if (editabledObject.cn !== undefined && editabledObject.cn !== null && editabledObject.cn !== '') updatedPhrase.cnStr = editabledObject.cn
  if (editabledObject.en !== undefined && editabledObject.en !== null && editabledObject.en !== '') updatedPhrase.enStr = editabledObject.en
  if (editabledObject.ru !== undefined && editabledObject.ru !== null && editabledObject.ru !== '') updatedPhrase.ruStr = editabledObject.ru

  // Извлечение и обработка массива words
  if (Array.isArray(editabledObject.words)) {
    updatedPhrase.words = editabledObject.words.map(word => {
      // Создаем новый объект только с непустыми ключами
      let filteredWord = {};
      if (word.cn !== undefined && word.cn !== null && word.cn !== '') filteredWord.cn = word.cn;
      if (word.en !== undefined && word.en !== null && word.en !== '') filteredWord.en = word.en;
      if (word.ru !== undefined && word.ru !== null && word.ru !== '') filteredWord.ru = word.ru;
      return filteredWord;
    }).filter(word => Object.keys(word).length > 0); // Исключаем пустые объекты
  }
  

  return updatedPhrase
};

