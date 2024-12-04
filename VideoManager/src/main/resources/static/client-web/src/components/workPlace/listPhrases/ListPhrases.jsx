import React, {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import Phrase from "./Phrase";
import AddNewPhraseAnd from "./AddNewPhraseAnd";
import {getTranslation} from "../../../services/phrasesService";
import {setTranslation} from "../../../store/reducers/translationReducer";
import {phraseReducer, setListPhrases} from "../../../store/reducers/phraseReduser";

/**
 * Компонент для отображения видео, над которым идет работа
 */
const ListPhrases = ({ translation_uuid }) => {
    const dispatch = useDispatch();
    const phrases = useSelector(state => state.phraseReducer.phrases);
    const translation = useSelector(state => state.translationReducer.translation)

    // Requesting phrases from the server
    useEffect(() => {
        if (!translation || translation.uuid!==translation_uuid) {
            getTranslation(translation_uuid)
                .then(response => {
                    dispatch(setTranslation(response))
                    dispatch(setListPhrases(response.phrases))
                })
                .catch(error => {
                    console.log(error);
                    throw error;
                })
        }
    }, [phrases]);

    return (
        <div className="listPhrases">
            {phrases && phrases.length > 0 ? (
                <div>
                    {phrases.map((phrase) => (
                        <Phrase phrase={phrase} key={phrase.id}/>
                    ))}
                    <AddNewPhraseAnd/>
                </div>
            ) : (
                <div>
                    <div style={{fontSize: 16, marginTop: 20}}>Фразы отсутствуют!</div>
                    <AddNewPhraseAnd/>
                </div>
            )}
        </div>
    );
};

export default ListPhrases;