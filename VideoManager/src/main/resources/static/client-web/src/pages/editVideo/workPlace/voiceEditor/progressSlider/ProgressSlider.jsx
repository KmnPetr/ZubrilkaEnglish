import { useEffect, useState } from "react";
import "./progressSlider.css";  // Подключаем файл с CSS-стилями
import RangeSlider from 'react-range-slider-input';
import 'react-range-slider-input/dist/style.css';

const ProgressSlider = ({ onChenge, maxValue = 1000, changedSignal }) => {
  const [sliderValue, setSliderValue] = useState([0, maxValue]);

  const handleSliderChange = (value) => {
    setSliderValue(value);
    onChenge(value);
  };
  useEffect(()=>{onChenge(sliderValue)},[sliderValue])

  // Сброс ползунка в дефолтное положение, когда меняется signal
  useEffect(() => {
    if (changedSignal) {
      setSliderValue([0, maxValue]);
    }
  }, [changedSignal, maxValue]);

  return (
    <RangeSlider
      min={0} 
      max={maxValue}
      value={sliderValue}
      onInput={setSliderValue}
      defaultValue={[0, maxValue]}
    />
  );
};

export default ProgressSlider;

