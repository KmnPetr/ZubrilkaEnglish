import './strCardFlipper.css';
import {SlArrowLeftCircle, SlArrowRightCircle} from "react-icons/sl";
import {useDispatch, useSelector} from "react-redux";
import {useEffect, useState} from "react";
import {openCardEditor} from "../../../../../redux/reducers/cardEditorReducer.js";
import {PHRASE, WORD} from "../../../../../redux/reducers/phraseReduser.js";

const StrCardFlipper = ()=>{
    const dispatch = useDispatch();
    const {isOpen, idPhrase, indexWord, typeStr, str, language} = useSelector((state) => state.cardEditorReducer);
    const phrases = useSelector(state => state.phraseReducer.phrases )
    const {used_languages,native_lang} = useSelector(state => state.videoInfoReducer)
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
        setCurrentIndexAction(index);
    },[idPhrase,indexWord,language,flatActions])

    //на основе phrases сформирует список str единый и слов и фраз без вложений для более удобного переключения на предыдущий и следующий str
    useEffect(() => {
        if (phrases && used_languages) {
            const flatActions = phrases.flatMap(phrase => {
                const { id } = phrase;

                // берем только строку native_lang того языка с которого переводим
                let phraseNativeStr = {
                    idPhrase: id,
                    indexWord: null, // не слово, значит null
                    str: phrase[native_lang]?.str ?? null,
                    language: native_lang,
                    typeStr: PHRASE
                }

                // Собираем строки из words
                let wordStrings = (phrase.words || []).map((word, index) =>
                    ({
                        idPhrase: id,
                        indexWord: index,
                        str: word?.[native_lang]?.str ?? null,
                        language: native_lang,
                        typeStr: WORD
                    })
                );

                return [phraseNativeStr, ...wordStrings];
            });
            setFlatActions(flatActions);
        }
    }, [phrases,used_languages,native_lang]);

    const goPrevious = () => dispatch(openCardEditor(flatActions[currentIndexAction - 1]))
    const goNext = () => dispatch(openCardEditor(flatActions[currentIndexAction + 1]))
    return (
        <div className="card_flipper_box">
            {currentIndexAction !== null && currentIndexAction !== undefined && currentIndexAction > 0 &&
                <SlArrowLeftCircle className="arrow clickable" onClick={goPrevious} />
            }
            <div className="spacer"></div> {/* Распорка */}
            {currentIndexAction !== null && currentIndexAction !== undefined && currentIndexAction < flatActions.length - 1 &&
                <SlArrowRightCircle className="arrow clickable" onClick={goNext} />
            }
        </div>
    )
}
export default StrCardFlipper;