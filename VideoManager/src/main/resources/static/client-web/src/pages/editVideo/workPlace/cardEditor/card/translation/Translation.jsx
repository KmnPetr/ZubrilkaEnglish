import './translation.css';
import {useSelector} from "react-redux";

const Translation=({className,card})=>{
    const {native_lang,used_languages} = useSelector(state => state.videoInfoReducer)

    return (<div className={`translation_box ${className}`}>
        <p>{JSON.stringify(card.translation)}</p>

        {used_languages &&
            used_languages.map(lang =>
                lang !== native_lang ? (
                    card.translation[lang]?.map((translation, index) => (
                        <div key={lang + index} className='translation_str'>
                            <p className="lang_name">{lang + ":"}</p>
                            <p>{translation}</p>
                        </div>
                    ))
                ) : null
            )}

    </div>)
}
export default Translation;