import './strFlipper.css';
import { SlArrowLeftCircle,SlArrowRightCircle } from "react-icons/sl";
import {useDispatch, useSelector} from "react-redux";
import {PHRASE} from "../../../redux/reducers/phraseReduser.js";
import {openVoiceEditor} from "../../../redux/reducers/voiceEditorReducer.js";
import {useEffect} from "react";

/**
 * компонент содержит логику и кнопки по перелистыванию строк враз и слов к следуюжей строке или предыдущей
 */
const StrFlipper = () => {
    const dispatch = useDispatch();
    const {isOpen, idPhrase, indexWord, typeStr, str, language} = useSelector((state) => state.voiceEditorReducer);
    const phrases = useSelector(state => state.phraseReducer.phrases )
    const {used_languages} = useSelector(state => state.videoInfoReducer)

    const nextLang = () => {
        const index = used_languages.indexOf(language)
        if (index < used_languages.length - 1) return used_languages[index + 1]
        else return null
    }

    const goPrevious = () => {
    }

    //найдет следующую строку
    const goNext = () => {
        if (typeStr === PHRASE) {
            if (nextLang()) {
                const nextLang2 = nextLang()
                const phrase = phrases.find(phrase => phrase.id === idPhrase)
                const nextStr = phrase[nextLang2].str
                dispatch(openVoiceEditor({idPhrase, indexWord: null, str:nextStr,language:nextLang2 , typeStr: PHRASE}))
            } else { /*польше других языков строк нет переходим к следующейфразе*/
            }
        }
    }
    return (
        <div className="str_flipper_box">
            <SlArrowLeftCircle className='arrow clickable' onClick={goPrevious}/>
            <SlArrowRightCircle className='arrow clickable' onClick={goNext}/>
        </div>
    )
};
export default StrFlipper;
