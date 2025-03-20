import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import Phrase from "./phrase/Phrase.jsx";
import AddNewPhraseAnd from "./phrase/addPhraseButtons/AddNewPhraseAnd.jsx";
import {getTranslation} from "../../../../api/phrasesService.js";
import {setTranslation,clearTranslation} from "../../../../redux/reducers/translationReducer.js";
import {setListPhrases,clearPhrases} from "../../../../redux/reducers/phraseReduser.js";

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