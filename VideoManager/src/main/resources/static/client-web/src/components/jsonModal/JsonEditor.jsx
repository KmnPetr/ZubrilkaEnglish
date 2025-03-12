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
  const {native_lang,used_languages} = useSelector(state => state.videoInfoReducer)

  const highlight = (jsonText) => Prism.highlight(jsonText, Prism.languages.json, 'json');

  const onClose = () => dispatch(closeJsonEditor()); // Закрывает окно
  const onCodeChange = (editableJson) => dispatch(updateJsonText(editableJson)); // Сохраняет изменения

  const apply =()=>{
    try {
      switch (typeObject) {
        case PHRASE:
          dispatch(updatePhrase(convertJsonToPhrase(jsonObject,editableJson,native_lang,used_languages)))
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
const convertJsonToPhrase = (jsonObject,editableJson,native_lang,used_languages) => {
  let updatedPhrase = jsonObject;
  const editabledObject = JSON.parse(editableJson);

  used_languages.forEach(lang => {
    if (!updatedPhrase[lang]) updatedPhrase[lang] = {};
    const newStr = editabledObject[lang]?.str ?? null;
    if (newStr !== null && newStr !== '') updatedPhrase[lang].str = newStr
    if (lang === native_lang){
      const transcription = editabledObject[lang]?.transcription ?? null;
      if (transcription !== null && transcription !== '') updatedPhrase[lang].transcription = transcription
    }
  })

  // Извлечение и обработка массива words
  if (Array.isArray(editabledObject.words)) {
    updatedPhrase.words = editabledObject.words.map(word => {
      // Создаем новый объект только с непустыми ключами
      let filteredWord = {};
      used_languages.forEach(lang => {
        filteredWord[lang] = {};
        const newStr = word[lang]?.str ?? null;
        if (newStr !== null && newStr !== '') filteredWord[lang].str = newStr
        if (lang === native_lang){
          const transcription = word[lang]?.transcription ?? null;
          if (transcription !== null && transcription !== '') filteredWord[lang].transcription = transcription
        }
      })
      return filteredWord;
    }).filter(word => Object.keys(word).length > 0); // Исключаем пустые объекты
  }
  

  return updatedPhrase
};

