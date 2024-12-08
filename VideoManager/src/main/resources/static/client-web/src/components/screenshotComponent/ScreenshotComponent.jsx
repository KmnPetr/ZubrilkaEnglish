import React, { useState, useRef, useEffect } from 'react';
import html2canvas from 'html2canvas';
import ScreenshotModal from './ScreenshotModal';

const ScreenshotComponent = () => {
    const [pressedKeys, setPressedKeys] = useState({a: false, s:false});
  const [isSelecting, setIsSelecting] = useState(false);
  const [coordinatesStart, setCoordinatesStart] = useState(null);
  const [coordinatesEnd, setCoordinatesEnd] = useState(null);
  const [screenshot, setScreenshot] = useState(null);
  


  useEffect(() => {
    
    // Функция для начала выделения
    const startSelection = () => {
        console.log(`Start selecting   `,coordinatesStart,` ||| `,coordinatesEnd)
    };
    
    // Функция для завершения выделения
    const finishSelection = () => {
        console.log(`Finish selecting   `,coordinatesStart,` ||| `,coordinatesEnd)

        takeScreenshot()

        setCoordinatesStart(null)
        setCoordinatesEnd(null)
      };
      const handleKeyDown = (e) => {
        const keyCode = e.keyCode; // Получаем код клавиши
        if (keyCode === 65 || keyCode === 83) { // 65 = 'a', 83 = 's'
            setPressedKeys((prev) => {
                const updatedKeys = { ...prev, [String.fromCharCode(keyCode).toLowerCase()]: true };
    
                if (updatedKeys['a'] && updatedKeys['s'] && !isSelecting) {
                    setIsSelecting(true);
                    startSelection();
                }
                return updatedKeys;
            });
        }
    };
    const handleKeyUp = (e) => {
        const keyCode = e.keyCode; // Получаем код клавиши
        if (keyCode === 65 || keyCode === 83) { // 65 = 'a', 83 = 's'
            setPressedKeys((prev) => ({ ...prev, [String.fromCharCode(keyCode).toLowerCase()]: false,}));
        }
    
        if (isSelecting) {
            setIsSelecting(false);
            finishSelection();
        }
    };

    const handleMouseMove = (e) => {
        if(isSelecting){
            if(!coordinatesStart){
                setCoordinatesStart({ x: e.clientX, y: e.clientY })
            } else {
                setCoordinatesEnd({ x: e.clientX, y: e.clientY });
            }
        }    
    };


    window.addEventListener('keydown', handleKeyDown);
    window.addEventListener('mousemove', handleMouseMove);
    window.addEventListener('keyup', handleKeyUp);

    return () => {
      window.removeEventListener('keydown', handleKeyDown);
      window.removeEventListener('mousemove', handleMouseMove);
      window.removeEventListener('keyup', handleKeyUp);
    };
  }, [pressedKeys,isSelecting,coordinatesStart,coordinatesEnd]);

  // Функция для вычисления ширины и высоты квадрата
  const getRectangleDimensions = () => {
    if (coordinatesStart && coordinatesEnd) {
      const width = Math.abs(coordinatesEnd.x - coordinatesStart.x);
      const height = Math.abs(coordinatesEnd.y - coordinatesStart.y);
      return { width, height };
    }
    return { width: 0, height: 0 };
  };

  const { width, height } = getRectangleDimensions();
  const topLeftX = coordinatesStart ? Math.min(coordinatesStart.x, coordinatesEnd?.x || 0) : 0;
  const topLeftY = coordinatesStart ? Math.min(coordinatesStart.y, coordinatesEnd?.y || 0) : 0;


  const takeScreenshot = () => {
    if (coordinatesStart && coordinatesEnd) {
        // Шаг 1: Создаем элемент, который будет использоваться для захвата части страницы.
        const element = document.createElement('div');
        element.style.position = 'absolute';
        element.style.top = `${topLeftY}px`;
        element.style.left = `${topLeftX}px`;
        element.style.width = `${width}px`;
        element.style.height = `${height}px`;

        // Шаг 2: Используем html2canvas для захвата области на странице.
        html2canvas(document.body, {
            x: topLeftX,  // Начальная позиция по оси X для захвата
            y: topLeftY,  // Начальная позиция по оси Y для захвата
            width: width, // Ширина области для захвата
            height: height // Высота области для захвата
        }).then((canvas) => {
            // Шаг 3: Преобразуем захваченное изображение в формат Base64
            const dataUrl = canvas.toDataURL();
            
            // Сохраняем изображение в состоянии
            setScreenshot(dataUrl);
        });
    }
};



  return (
    <div>
      <div
        style={{
          position: 'absolute',
          top: `${topLeftY}px`,
          left: `${topLeftX}px`,
          width: `${width}px`,
          height: `${height}px`,
          backgroundColor: 'transparent',
          border: '1px solid white',
          zIndex: 9999, // Обеспечивает, чтобы квадрат был поверх других элементов
          pointerEvents: 'none', // Это позволяет мыши взаимодействовать с элементами под квадратом
        }}
      ></div>
      {screenshot && (
        <ScreenshotModal screenshot={screenshot} onClose={() => setScreenshot(null)} />
      )}
    </div>
  );
};

export default ScreenshotComponent;