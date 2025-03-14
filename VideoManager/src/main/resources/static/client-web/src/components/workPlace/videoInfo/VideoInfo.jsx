import {useEffect, useRef, useState} from 'react';
import "../../../css/WorkPlace.css";
import { getVideoInfoByUuid } from "../../../api/videoService";
import EditInfo from "./EditInfo";
import {updateUsedLanguagesField, updateVideoInfoField} from "../../../api/videoInfoService";
import {downloadIcon} from "../../../api/iconService"
import {useDispatch, useSelector} from "react-redux";
import './VideoInfo.css'
import {clearVideoInfo, setVideoInfo} from "../../../redux/reducers/videoInfoReducer.js";

const languages = ["cn", "en", "ru"];

const VideoInfo = ({videoInfo_uuid}) => {
    const dispatch = useDispatch();
    const videoInfo = useSelector(state => state.videoInfoReducer)
    const setVideoInfo2 =(videoInfo)=>{
        dispatch(setVideoInfo(videoInfo))
    }

    const [loading, setLoading] = useState(true); // Состояние для отображения загрузки
    const [error, setError] = useState(null); // Состояние для ошибок
    const [showJson, setShowJson] = useState(false); // Состояние для управления видимостью JSON
    const [imageUrl, setImageUrl] = useState(null);
    const isIconChanged = useSelector(state => state.networkReducer.network.isIconChanged);
    const prevNativeLangRef = useRef(null);//хранит предыдущее значение videoInfo.native_lang во избежании зацикливания запросов к серверу

    //занимается синхронизацией полей videoInfo.native_lang и videoInfo.used_languages
    useEffect(() => {
        if (videoInfo){
            const prevNativeLang = prevNativeLangRef.current; // Получаем предыдущее значение

            // Если native_lang не изменился, выходим из useEffect
            if (videoInfo.native_lang === prevNativeLang) {
                return;
            } else prevNativeLangRef.current = videoInfo.native_lang;

            if (videoInfo.native_lang!==null&&videoInfo.native_lang!==""){
                let newUsedLang = videoInfo.used_languages
                if (!videoInfo.used_languages.includes(videoInfo.native_lang)){
                    newUsedLang.push(videoInfo.native_lang);
                }
                newUsedLang.sort((a,b)=>b===videoInfo.native_lang ? 1 : -1)
                updateUsedLanguagesField(videoInfo_uuid, newUsedLang)
                    .then(response => {
                        setVideoInfo2(response);
                    })
                    .catch(e => {setError(e.message)})
            }
        }
    }, [videoInfo]);

    useEffect(() => {
        downloadIcon(videoInfo_uuid)
            .then(imageUrl => {
                setImageUrl(imageUrl)
            })
            .catch();
    }, [isIconChanged]);

    // Запрос данных при загрузке компонента или изменении videoInfo_uuid
    useEffect(() => {
        getVideoInfoByUuid(videoInfo_uuid)
            .then(response => {
                setVideoInfo2(response);
                setLoading(false);
            })
            .catch(err => {
                setError(err.message);
            })
            .finally(() => {
                setLoading(false);
            });

        return () => {
            dispatch(clearVideoInfo())
        };
    }, [videoInfo_uuid]);

    const onUpdateVideoInfoField = (fieldName, newValue) => {
        setLoading(true);
        updateVideoInfoField(videoInfo_uuid, fieldName, newValue)
            .then(response => {
                setVideoInfo2(response);
                setLoading(false);
            })
            .catch(err => {
                setError(err.message);
            })
            .finally(() => {
                setLoading(false);
            });
    };

    // Функция для переключения состояния видимости JSON
    const toggleJsonVisibility = () => {
        setShowJson(!showJson);
    };

    const onClickChekbox = (lang) =>{
        if (videoInfo){
            let newUsedLang = videoInfo.used_languages
            if(videoInfo.used_languages.includes(lang)&&lang!==videoInfo.native_lang) newUsedLang = newUsedLang.filter(el => el !== lang)
            else if(!videoInfo.used_languages.includes(lang)&&lang!==videoInfo.native_lang) newUsedLang.push(lang)

            newUsedLang.sort((a, b)=>b===videoInfo.native_lang ? 1 : -1)

            //Отправляем на сервер
            updateUsedLanguagesField(videoInfo_uuid, newUsedLang)
                .then(response => {
                    setVideoInfo2(response);
                })
                .catch(e => {setError(e.message)})
        }
    }

    // Визуализация компонента
    return (
        <div>
            <details>
                <summary>Additional information</summary>
                <div className='videoInfo_box'>
                    <div>
                        {loading && <p>Загрузка...</p>}
                        {error && <p style={{color: 'red'}}>Ошибка: {error}</p>}
                        {videoInfo && (
                            <div>
                                <div>
                                    {imageUrl && <img src={imageUrl} className="icon"/>}
                                </div>
                                <div>
                                    <EditInfo fieldName={'cnName'} fieldNameAlias={'Video cn_name'}
                                              fieldValue={videoInfo.cnName}
                                              onUpdateVideoInfoField={onUpdateVideoInfoField}/>
                                    <EditInfo fieldName={'enName'} fieldNameAlias={'Video en_name'}
                                              fieldValue={videoInfo.enName}
                                              onUpdateVideoInfoField={onUpdateVideoInfoField}/>
                                    <EditInfo fieldName={'ruName'} fieldNameAlias={'Video ru_name'}
                                              fieldValue={videoInfo.ruName}
                                              onUpdateVideoInfoField={onUpdateVideoInfoField}/>
                                    <EditInfo fieldName={'linkOriginal'} fieldNameAlias={'Link to the original'}
                                              fieldValue={videoInfo.linkOriginal}
                                              onUpdateVideoInfoField={onUpdateVideoInfoField}/>
                                </div>
                                <h3 onClick={toggleJsonVisibility}
                                    style={{cursor: 'pointer', textDecoration: 'underline', fontSize: '12px'}}>
                                    {showJson ? "Hide detailed video information" : "Show detailed video information"}
                                </h3>
                                {showJson && (
                                    <pre>{JSON.stringify(videoInfo, null, 2)}</pre>
                                )}
                            </div>
                        )}
                    </div>
                    <div className='language_selector'>
                        <div>
                            <p>Select the language of the original video</p>
                            {videoInfo &&
                                <select
                                    value={videoInfo.native_lang ?? ""}
                                    onChange={(e) => onUpdateVideoInfoField('native_lang', e.target.value)}
                                    className="native_lang_select"
                                >
                                    <option value="">—</option>
                                    {languages.map((lang) => (
                                        <option key={lang} value={lang}>
                                            {lang}
                                        </option>
                                    ))}
                                </select>}
                        </div>
                        <div className="checkbox_container">
                            <p>Select the languages of the translation</p>
                            {languages.map((lang) => (
                                <label key={lang}>
                                    <input
                                        type="checkbox"
                                        checked={videoInfo?.used_languages?.includes(lang)}
                                        onChange={()=>onClickChekbox(lang)}
                                        disabled={lang === videoInfo?.native_lang}
                                    />
                                    {lang}
                                </label>
                            ))}
                        </div>
                    </div>
                </div>
            </details>
        </div>
    );
};

export default VideoInfo;