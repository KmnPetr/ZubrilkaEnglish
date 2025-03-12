import './strFlipper.css';
import { SlArrowLeftCircle,SlArrowRightCircle } from "react-icons/sl";
import {useDispatch, useSelector} from "react-redux";
import {PHRASE, WORD} from "../../../redux/reducers/phraseReduser.js";
import {openVoiceEditor} from "../../../redux/reducers/voiceEditorReducer.js";
import {useEffect, useState} from "react";

/**
 * компонент содержит логику и кнопки по перелистыванию строк враз и слов к следуюжей строке или предыдущей
 */
const StrFlipper = () => {
    const dispatch = useDispatch();
    const {isOpen, idPhrase, indexWord, typeStr, str, language} = useSelector((state) => state.voiceEditorReducer);
    const phrases = useSelector(state => state.phraseReducer.phrases )
    const {used_languages} = useSelector(state => state.videoInfoReducer)
    const [flatActions, setFlatActions] = useState(null)
    const [currentIndexAction, setCurrentIndexAction] = useState(null)

    //вычислит текущий индекс на основе текущего экшена
    useEffect(() => {
        if (!flatActions) return;

        const index = flatActions.findIndex(action =>
            action.idPhrase === idPhrase &&
            action.indexWord === indexWord &&
            action.language === language
        );
        console.log('index='+index)
        setCurrentIndexAction(index);
    },[idPhrase,indexWord,language,flatActions])

    //на основе phrases сформирует список str единый и слов и фраз без вложений для более удобного переключения на предыдущий и следующий str
    useEffect(() => {
        if (phrases && used_languages) {
            const flatActions = phrases.flatMap(phrase => {
                const { id } = phrase;

                // Собираем строки из основной фразы
                let mainStrings = used_languages.map(lang => ({
                    idPhrase: id,
                    indexWord: null, // не слово, значит null
                    str: phrase[lang]?.str ?? null,
                    language: lang,
                    typeStr: PHRASE
                }));

                // Собираем строки из words
                let wordStrings = (phrase.words || []).flatMap((word, index) =>
                    used_languages.map(lang => ({
                        idPhrase: id,
                        indexWord: index,
                        str: word?.[lang]?.str ?? null,
                        language: lang,
                        typeStr: WORD
                    }))
                );

                return [...mainStrings, ...wordStrings];
            });
            setFlatActions(flatActions);
            console.log('flatActions='+JSON.stringify(flatActions,null,2))
        }
    }, [phrases,used_languages]);

    const goPrevious = () => dispatch(openVoiceEditor(flatActions[currentIndexAction - 1]))
    const goNext = () => dispatch(openVoiceEditor(flatActions[currentIndexAction + 1]))
    // Определяем, есть ли предыдущий/следующий экшен
    return (
        <div className="str_flipper_box">
            {currentIndexAction !== null && currentIndexAction !== undefined && currentIndexAction > 0 &&
                <SlArrowLeftCircle className="arrow clickable" onClick={goPrevious} />
            }
            <div className="spacer"></div> {/* Распорка */}
            {currentIndexAction !== null && currentIndexAction !== undefined && currentIndexAction < flatActions.length - 1 &&
                <SlArrowRightCircle className="arrow clickable" onClick={goNext} />
            }
        </div>
    )
};
export default StrFlipper;
