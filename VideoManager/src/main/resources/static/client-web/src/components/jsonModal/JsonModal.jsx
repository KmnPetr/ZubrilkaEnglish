import React, { useState } from 'react';
import Editor from 'react-simple-code-editor';
import Prism from 'prismjs';
import 'prismjs/themes/prism-okaidia.css'; //тема подсветки синтаксиса можно поменять
import 'prismjs/components/prism-json'; // Поддержка JSON
import ModalWindow from '../ui/ModalWindow';

const JsonEditor = () => {
    const [code, setCode] = useState(`{
  "name": "John",
  "age": 30,
  "city": "New York"
}`);

    const highlight = (code) =>
        Prism.highlight(code, Prism.languages.json, 'json');

    return (
      <ModalWindow isOpen={true} onClose={()=>{}}>
        <div style={{ fontFamily: 'monospace', fontSize: '14px' }}>
            <Editor
                value={code}
                onValueChange={setCode}
                highlight={highlight}
                padding={10}
                style={{
                    backgroundColor: '#282828',
                    border: '1px solid #303030',
                    borderRadius: '4px',
                    minHeight: '200px',
                }}
            />
        </div>
      </ModalWindow>
    );
};

export default JsonEditor;
