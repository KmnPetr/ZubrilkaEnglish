import './selectVoice.css'
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';
import NativeSelect from '@mui/material/NativeSelect';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { useEffect, useState } from 'react';
import { getListVoices } from '../../../../services/zvukogramService';
import { defaultLanguages } from './defaultLanguages';
import { SlUser,SlUserFemale } from "react-icons/sl";
import { Autocomplete, TextField,FormControlLabel,Checkbox } from '@mui/material';

const CN = 'cn'
const EN = 'en'
const RU = 'ru' 

const SelectVoice=({onSelect,language,onSelectUseApi})=>{
    const [lang_selector,setLangSelector] = useState(language)
    const [languages,setLanguages] = useState(defaultLanguages)
    const [selectedLanguage, setSelectedLanguage] = useState(defaultLanguages[0].originKey);
    const [allVoices,setAllVoices] = useState(null);
    const [filteredVoices,setFilteredVoices] = useState([])
    const [selectedVoice,setSelectedVoice] = useState("")
    const [useApi, setUseApi] = useState(true);
    useEffect(()=>{
        getListVoices().then(r=>{
            console.log(r)
            setAllVoices(r);
        }) //получение списка голосов с сайта звукограмм
    },[])
    useEffect(()=>{
        const filteredList = [
            ...defaultLanguages.filter(it => it.family === lang_selector)
          ];
        setLanguages(filteredList)
    },[lang_selector])
    useEffect(()=>{
        setSelectedLanguage(languages[0].originKey)
    },[languages])
    useEffect(()=>{
        if (allVoices&&allVoices[selectedLanguage]) {
            setFilteredVoices(allVoices[selectedLanguage])
        }
    },[selectedLanguage,allVoices])

    useEffect(()=>{
        if(filteredVoices&&filteredVoices[0]) setSelectedVoice(filteredVoices[0].voice)
    },[filteredVoices])

    useEffect(()=>{},[selectedVoice])

    const handleLanguageChange = (e) => {
        setSelectedLanguage(e.target.value)
    }
    const onSelectVoice =(newValue)=> {
        setSelectedVoice(newValue)
        onSelect(newValue)
    }

    const changeUseApi = (event) => {
        setUseApi(event.target.checked);
        onSelectUseApi(event.target.checked)
    };

    return(
        <div className="select_voice">

            <ThemeProvider theme={darkTheme}>
                <Autocomplete
                    options={filteredVoices}
                    getOptionLabel={(option) => option.voice}
                    sx={{ width: 250 }}
                    value={filteredVoices.find(v => v.voice === selectedVoice) || null}
                    onChange={(e, newValue) => onSelectVoice(newValue ? newValue.voice : null)}
                    disableClearable
                    renderOption={(props, option) => (
                        <li {...props} style={{ display: 'flex', alignItems: 'center' }}>
                            {option.sex==='male'&&<SlUser className='male_icon'/>}
                            {option.sex==='female'&&<SlUserFemale className='female_icon'/>}
                            <p>{option.voice}</p>
                            {option.pro === '1' && <p className='pro'>pro</p>}
                        </li>)}
                    renderInput={(params) => {
                        const option = filteredVoices.find(v => v.voice === selectedVoice); // Получаем выбранную опцию
                        return (
                            <TextField {...params} label="voice" value={selectedVoice || ''}
                                InputProps={{
                                    ...params.InputProps,
                                    startAdornment: option ? (
                                        <div style={{ display: 'flex', alignItems: 'center' }}>
                                            {option.sex === 'male' && <SlUser className="male_icon" />}
                                            {option.sex === 'female' && <SlUserFemale className="female_icon" />}
                                            {option.pro === '1' && <p className='pro'>pro</p>}
                                        </div>
                                    ) : null,
                                }}
                            />
                        );
                    }}
                />

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
                        onChange={(e)=>setLangSelector(e.target.value)}
                        inputProps={{name: 'language_selector',id: 'uncontrolled-native',}}>
                        <option value={CN}>{CN}</option>
                        <option value={EN}>{EN}</option>
                        <option value={RU}>{RU}</option>
                        </NativeSelect>
                    </FormControl>
                </Box>
                
                <FormControlLabel
                    control={
                        <Checkbox
                            checked={useApi}          // Состояние чекбокса зависит от useApi
                            onChange={changeUseApi}  // Обработчик изменения состояния
                        />
                    }
                    label="use api"
                />
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