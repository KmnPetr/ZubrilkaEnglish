import './selectVoice.css'
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';
import NativeSelect from '@mui/material/NativeSelect';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { useEffect, useState } from 'react';
import { getListVoices } from '../../../../../../api/zvukogramService.js';
import { defaultLanguages } from './defaultLanguages.js';
import CastomVoiceSelector from "./castomVoiceSelector/CastomVoiceSelector.jsx";
import {getRatingVoices} from "../../../../../../api/personService.js";
import {useDispatch} from "react-redux";
import {updateRatingVoicesRedux} from "../../../../../../redux/reducers/authReducer.js";

const CN = 'cn'
const EN = 'en'
const RU = 'ru'

const SelectVoice=({onSelect,language})=>{
    const dispatch = useDispatch()
    const [lang_selector,setLangSelector] = useState(language)
    const [languages,setLanguages] = useState(defaultLanguages)
    const [selectedLanguage, setSelectedLanguage] = useState(defaultLanguages[0].originKey);
    const [allVoices,setAllVoices] = useState(null);
    const [filteredVoices,setFilteredVoices] = useState([])
    const [selectedVoice,setSelectedVoice] = useState(null/*{voice:'',sex:''}*/)
    const [ratingVoices,setRatingVoices] = useState({})//значения предпочтения голоса, определяется по звездочкам по 3х бальной шкале

    useEffect(()=>{dispatch(updateRatingVoicesRedux(ratingVoices)) /*пригодится в другом компоненте*/},[ratingVoices])

    useEffect(()=>{setLangSelector(language)},[language])

    //запросит ratingVoices с сервера
    useEffect(()=>{
        getRatingVoices()
            .then(data=>{
                if (data){
                    setRatingVoices(data)
                    setFilteredVoices(sortFilteredVoices(data,filteredVoices))
                }

            })
            .catch(err=>{console.error(err)})
    },[])

    //при получении новых данных рейтинга голосов или при получении нового списка голосов отсортирует по рейтингу
    const sortFilteredVoices=(ratingVoices,voices)=>{
        const newFilteredVoices = voices.sort((a, b) => {
            const ratingA = ratingVoices[a.voice];
            const ratingB = ratingVoices[b.voice];
            // Если ratingA или ratingB - null или undefined, присваиваем им минимальное значение
            const safeRatingA = (ratingA === null || ratingA === undefined) ? -1 : ratingA;
            const safeRatingB = (ratingB === null || ratingB === undefined) ? -1 : ratingB;
            return safeRatingB - safeRatingA;
        });
        return newFilteredVoices;
    }

    //получение списка голосов с сайта звукограмм
    useEffect(()=>{
        getListVoices().then(r=>{
            setAllVoices(r);
        }) 
    },[])
    
    //устанавливает список языков по первому селектору языков
    useEffect(()=>{
        const filteredList = [
            ...defaultLanguages.filter(it => it.family === lang_selector)
          ];
        setLanguages(filteredList)
    },[lang_selector])

    //при изменении языков первый язык устанавливает в выбранный
    useEffect(()=>{
        setSelectedLanguage(languages[0].originKey)
    },[languages])

    useEffect(()=>{
        if (allVoices&&allVoices[selectedLanguage]) {
            setFilteredVoices(sortFilteredVoices(ratingVoices,allVoices[selectedLanguage]))
        }
    },[selectedLanguage,allVoices])

    useEffect(()=>{
        if(filteredVoices&&filteredVoices[0]) setSelectedVoice({voice:filteredVoices[0].voice,sex:filteredVoices[0].sex})
    },[filteredVoices])

    useEffect(()=>{
        onSelect(selectedVoice)
    },[selectedVoice])

    const handleLanguageChange = (e) => {
        setSelectedLanguage(e.target.value)
    }

    const onSelectVoice =(voice)=> {
        setSelectedVoice(voice)
    }

    const onClickRating=(event, newValue,voice)=>{
        const newRatingVoices = { ...ratingVoices, [voice]: newValue };
        if (newValue === null) delete newRatingVoices[voice];

        setRatingVoices(newRatingVoices);
        updateRatingVoices({newRatingVoices: newRatingVoices,oldRatingVoices: ratingVoices})
            .then(()=>{})
            .catch((oldRatingVoices,error)=>{
                setRatingVoices(oldRatingVoices);
                console.error(error)})
    }

    return(
        <div className="select_voice">

            <CastomVoiceSelector
                className='c_voice_selector'
                selectedVoice={filteredVoices.find(v => v.voice === selectedVoice?.voice) || null}
                listVoice={filteredVoices}
                onSelectVoice={onSelectVoice}
                onClickRating={onClickRating}
                ratingVoices={ratingVoices}
            />

            <ThemeProvider theme={darkTheme}>
                <Box sx={{ minWidth: 120 }}>
                    <FormControl fullWidth>
                        <InputLabel variant="standard" htmlFor="uncontrolled-native" shrink>language</InputLabel>
                        <NativeSelect
                        value={selectedLanguage.originKey}
                        onChange={handleLanguageChange}
                        inputProps={{name: 'language',id: 'uncontrolled-native',}}>
                            {languages && languages.map(it=>(
                                <option value={it.originKey} key={it.originKey}>{it.name}</option>
                            ))}
                        </NativeSelect>
                    </FormControl>
                </Box>
                <Box sx={{ minWidth: 90 }}>
                    <FormControl fullWidth>
                        <InputLabel variant="standard" htmlFor="uncontrolled-native" shrink>selector</InputLabel>
                        <NativeSelect
                        defaultValue={language}
                        value={language}
                        onChange={(e)=>setLangSelector(e.target.value)}
                        inputProps={{name: 'language_selector',id: 'uncontrolled-native',}}>
                        <option value={CN}>{CN}</option>
                        <option value={EN}>{EN}</option>
                        <option value={RU}>{RU}</option>
                        </NativeSelect>
                    </FormControl>
                </Box>
            </ThemeProvider>
        </div>
    )
}
export default SelectVoice


const darkTheme = createTheme({
    palette: {
      mode: 'dark',
      primary: {
        main: '#90caf9', // Голубой цвет (Material UI)
      },
      background: {
        default: '#121212', // Тёмный фон
        paper: '#1e1e1e',   // Тёмный цвет компонентов
      },
      text: {
        primary: '#ffffff', // Белый текст
        secondary: '#b0bec5', // Серый текст
      },
    },
  });
