import React from 'react';
import Editor from 'react-simple-code-editor';
import Prism from 'prismjs';
import 'prismjs/themes/prism-okaidia.css'; // Тема подсветки синтаксиса
import 'prismjs/components/prism-json'; // Поддержка JSON
import ModalWindow from '../ui/ModalWindow';
import { useDispatch, useSelector } from 'react-redux';
import { closeJsonEditor, setJsonCode } from '/src/store/reducers/jsonEditorReducer.js';

const JsonEditor = () => {
  const dispatch = useDispatch();
  const { isOpen, code } = useSelector((state) => state.jsonEditorReducer);

  const highlight = (code) => Prism.highlight(code, Prism.languages.json, 'json');

  const onClose = () => dispatch(closeJsonEditor()); // Закрывает окно
  const onCodeChange = (newCode) => dispatch(setJsonCode(newCode)); // Сохраняет изменения

  return (
    <ModalWindow isOpen={isOpen} onClose={onClose} width="70%" height="70%">
      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          height: '100%',
        }}
      >
        <div
          style={{
            flex: 1,
            overflow: 'auto', // Прокрутка для длинного содержимого
            backgroundColor: '#282828',
            border: '1px solid #303030',
            borderRadius: '4px',
          }}
        >
          <Editor
            value={code}
            onValueChange={onCodeChange}
            highlight={highlight}
            padding={10}
            style={{
              minHeight: '100%',
              fontFamily: 'monospace',
              fontSize: '14px',
              color: '#fff',
            }}
          />
        </div>
      </div>
    </ModalWindow>
  );
};

export default JsonEditor;
