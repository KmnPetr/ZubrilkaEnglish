import './selectVoice.css'
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';
import NativeSelect from '@mui/material/NativeSelect';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { useEffect, useState } from 'react';
import { getListVoices } from '../../../../services/zvukogramService';
import { defaultLanguages } from './defaultLanguages';

const CN = 'cn'
const EN = 'en'
const RU = 'ru' 

const SelectVoice=({onSelect,language})=>{
    const [lang_selector,setLangSelector] = useState(language)
    const [languages,setLanguages] = useState(defaultLanguages)
    const [selectedLanguage, setSelectedLanguage] = useState(defaultLanguages[0].originKey);
    const [allVoices,setAllVoices] = useState(null);
    const [filteredVoices,setFilteredVoices] = useState([])
    const [selectedVoice,setSelectedVoice] = useState("")
    useEffect(()=>{
        getListVoices().then(r=>{
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
        console.log(`useEffect selectedLanguage: ${selectedLanguage}`)
        if (allVoices&&allVoices[selectedLanguage]) {
            setFilteredVoices(allVoices[selectedLanguage])
            setSelectedVoice(allVoices[selectedLanguage][0].voice)
        }
    },[selectedLanguage,allVoices])

    useEffect(()=>{console.log('selectedVoice: '+selectedVoice)},[selectedVoice])

    const handleLanguageChange = (e) => {
        console.log('handleLanguageChange '+e.target.value)
        setSelectedLanguage(e.target.value)
    }
    const onSelectVoice =(e)=> {
        setSelectedVoice(e.target.value)
        onSelect(e.target.value)
    }

    return(
        <div className="select_voice">

            <ThemeProvider theme={darkTheme}>
                <Box sx={{ minWidth: 120 }}>
                    <FormControl fullWidth>
                        <InputLabel variant="standard" htmlFor="uncontrolled-native" shrink>voice</InputLabel>
                        <NativeSelect
                        value={selectedVoice}
                        onChange={onSelectVoice}
                        inputProps={{name: 'voice',id: 'uncontrolled-native',}}>
                            {filteredVoices.length!==0 ? (
                                filteredVoices.map(it=>(<option value={it.voice} key={it.voice}>{it.voice}</option>))
                            ):(<option value=""></option>)}
                        </NativeSelect>
                    </FormControl>
                </Box>

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