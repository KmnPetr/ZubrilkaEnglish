import { useEffect, useState } from "react";

//вычисляет интервал времени начала и конца воспроизведения отрезка аудио
const useInterval=(duration)=>{
    const maxValue = 1000;
    const [[startValue, endValue],setValues] = useState([0,maxValue])
    const [[startTime, endTime],setInterval] = useState([0,0])

    const changeInterval = ([newStartValue, newEndValue]) => {
        if (typeof newStartValue === 'number' && typeof newEndValue === 'number' && !isNaN(newStartValue) && !isNaN(newEndValue)){
            setValues([newStartValue, newEndValue]);
        }else{console.log("else 1")}
    }

    useEffect(()=>{
        console.log(`startValue ${startValue}, endValue ${endValue}`)
        console.log(`duration ${duration}`)
        if (typeof duration === 'number' && !isNaN(duration) && duration >= 0 && duration !== Infinity && duration !== -Infinity) {
            const startTime = duration/maxValue*startValue
            const endTime = duration/maxValue*endValue
            setInterval([startTime,endTime])
        }else{console.log("else 2")}
    },[duration,startValue,endValue])

    return{startTime,endTime,changeInterval,maxValue}
}
export default useInterval