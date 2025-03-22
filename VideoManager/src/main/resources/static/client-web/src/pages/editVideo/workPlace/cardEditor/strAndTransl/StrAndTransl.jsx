import './strAndTransl.css';
import {useSelector} from "react-redux";
import Str from "../../../../../ui/str/Str.jsx";

/**
 * что-то среднее между Str Phrase и Word
 * содержит основную строку
 * а также ее переводы на другие языки с возможностью редактировать
 */
const StrAndTransl=({className,strObj,typeStr,idPhrase,indexWord})=>{
    const {native_lang,used_languages} = useSelector(state => state.videoInfoReducer)
    return (<div className={`str_transl_box ${className}`}>
        {used_languages && used_languages.map(lang =>
            <Str
                key={lang}
                inCardEditor={true}
                str={strObj[lang]}
                typeStr={typeStr}
                idPhrase={idPhrase}
                indexWord={indexWord}
                language={lang}
                isHover={true}
                isNativeLang={false}/>)}
    </div>);
}
export default StrAndTransl;