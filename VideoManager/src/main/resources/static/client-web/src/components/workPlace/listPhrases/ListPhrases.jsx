import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import Phrase from "./Phrase";
import AddNewPhraseAnd from "./AddNewPhraseAnd";
import {getTranslation} from "../../../services/phrasesService";
import {setTranslation,clearTranslation} from "../../../store/reducers/translationReducer";
import {setListPhrases,clearPhrases} from "../../../store/reducers/phraseReduser";

const ListPhrases = ({ videoInfo_uuid }) => {
    const dispatch = useDispatch();
    const phrases = useSelector(state => state.phraseReducer.phrases);
    const translation = useSelector(state => state.translationReducer.translation)


    useEffect(() => {
        getTranslation(videoInfo_uuid)
            .then(response => {
                dispatch(setTranslation(response))
                dispatch(setListPhrases(response.phrases))
            })
            .catch(error => {
                console.log(error);
                throw error;
            })
        return () => {
            dispatch(clearTranslation())
            dispatch(clearPhrases())
        };
    }, []);

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
                    <div style={{fontSize: 16, marginTop: 20}}>There are no phrases!</div>
                    <AddNewPhraseAnd/>
                </div>
            )}
        </div>
    );
};

export default ListPhrases;