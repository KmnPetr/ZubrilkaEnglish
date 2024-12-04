import React from "react";
import {useDispatch} from "react-redux";
import {TbChevronsLeft, TbChevronLeft, TbChevronRight, TbChevronsRight} from "react-icons/tb";
import { IoPlay } from "react-icons/io5";
import {setIntervalAction} from "../../../store/reducers/videoManagementReducer";
import {editTime} from "../../../store/reducers/phraseReduser";

/**
 * компонент отвечает за отображение всевозможной информации по интервалу видео к которому пренадлежит фраза
 */
const PhraseInterval = ({phrase}) => {
    const dispatch = useDispatch();
    const startTime = 'startTime'
    const endTime = 'endTime'

    const onClickPlay = () => {
        dispatch(setIntervalAction(phrase))
    }
    // отправит новое значение
    const changeTime = (newValue, fieldName) => {
        let newValue1 = newValue
        if (/[^0-9]/.test(newValue1)) return; // Прерываем выполнение функции, если найдены символы, кроме цифр
        if (newValue1 === '') newValue1 = '0';
        newValue1 = newValue1 * 1 //чтобы убрать нолик впереди

        dispatch(editTime(phrase.id, newValue1, fieldName));

        //скорректируем значения начала и конца воспроизведения чтобы они не перехлестывались
        if (fieldName === startTime){
            if (newValue1>phrase.endTime)
                dispatch(editTime(phrase.id, newValue1+400, endTime));
        }else if (fieldName === endTime){
            if (newValue1<phrase.startTime){
                const newStartTime = newValue1-400
                if (newStartTime<0){
                    dispatch(editTime(phrase.id, 0, startTime));
                }else dispatch(editTime(phrase.id, newStartTime, startTime));
            }
        }
    };
    //отправит новое значение со стрелок
    const editArrow = (supplement, fieldName) => {
        const newValue = phrase[fieldName]+supplement
        changeTime(newValue, fieldName)
    }

    return (
        <div className="phrase-interval">

            <p style={{fontSize:'10px'}}>Start:</p>
            <TbChevronsLeft className='clickable' onClick={() => editArrow(-1000, startTime)}/>
            <TbChevronLeft className='clickable' onClick={() => editArrow(-200, startTime)}/>
            <input type="text" value={phrase.startTime} onChange={(e) => changeTime(e.target.value, startTime)}/>
            <TbChevronRight className='clickable' onClick={() => editArrow(200, startTime)}/>
            <TbChevronsRight className='clickable' onClick={() => editArrow(1000, startTime)}/>


            <p style={{fontSize:'10px',marginLeft: '10px'}}>End:</p>
            <TbChevronsLeft className='clickable' onClick={() => editArrow(-1000, endTime)}/>
            <TbChevronLeft className='clickable' onClick={() => editArrow(-200, endTime)}/>
            <input type="text" value={phrase.endTime} onChange={(e) => changeTime(e.target.value, endTime)}/>
            <TbChevronRight className='clickable' onClick={() => editArrow(200, endTime)}/>
            <TbChevronsRight className='clickable' onClick={() => editArrow(1000, endTime)}/>

            <IoPlay onClick={onClickPlay} className="phrase-interval-play clickable"/>
        </div>
    );
};

export default PhraseInterval;