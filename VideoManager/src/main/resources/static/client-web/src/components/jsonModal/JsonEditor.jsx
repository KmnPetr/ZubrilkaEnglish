import React from 'react';
import Editor from 'react-simple-code-editor';
import Prism from 'prismjs';
import 'prismjs/themes/prism-okaidia.css'; // Тема подсветки синтаксиса
import 'prismjs/components/prism-json'; // Поддержка JSON
import ModalWindow from '../ui/ModalWindow';
import { useDispatch, useSelector } from 'react-redux';
import { closeJsonEditor, updateJsonText } from '/src/store/reducers/jsonEditorReducer.js';
import './JsonEditor.css';
import CButton from '../ui/CButton';
import { PHRASE } from '../../store/reducers/jsonEditorReducer';
import { updatePhrase } from '../../store/reducers/phraseReduser';

const JsonEditor = () => {
  const dispatch = useDispatch();
  const { isOpen,jsonObject, editableJson,typeObject } = useSelector((state) => state.jsonEditorReducer);

  const highlight = (jsonText) => Prism.highlight(jsonText, Prism.languages.json, 'json');

  const onClose = () => dispatch(closeJsonEditor()); // Закрывает окно
  const onCodeChange = (editableJson) => dispatch(updateJsonText(editableJson)); // Сохраняет изменения

  const apply =()=>{
    try {
      switch (typeObject) {
        case PHRASE:
          dispatch(updatePhrase(convertJsonToPhrase(jsonObject,editableJson)))
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

const convertJsonToPhrase = (jsonObject, editableJson) => {
  let updatedPhrase = jsonObject;
  const editabledObject = JSON.parse(editableJson);

  updatedPhrase = {...updatedPhrase,...editabledObject}

  return updatedPhrase
};