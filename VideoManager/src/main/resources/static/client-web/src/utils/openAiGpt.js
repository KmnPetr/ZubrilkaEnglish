import { copyToClipboard } from "./copyToClipboard";

export const openAiGptRequest =(nativeStr,native_lang,used_languages)=>{
    const ruText = getRuText(nativeStr,native_lang,used_languages)
    //скопирует запрос строку в буфер
    copyToClipboard(ruText)
    //перенаправит в чат gpt и задаст ему вопрос
    redirectToChatGPT(ruText);
}



function redirectToChatGPT(query) {
    // Формируем URL с переданным запросом
    const baseUrl = "https://chat.openai.com/";
    const encodedQuery = encodeURIComponent(query);
    const finalUrl = `${baseUrl}?q=${encodedQuery}`;

    // Открываем URL в новой вкладке
    window.open(finalUrl, '_blank');
}


const getRuText = (nativeStr,native_lang,used_languages) => {

    const jsonExample = {};
    used_languages.forEach(lang => {
        if(lang === native_lang){
            jsonExample[lang] = {str:nativeStr,transcription:null}
        } else{
            jsonExample[lang] = {str:null}
        }
    })
    jsonExample.words = Array
        .from({length: 2}, (_, i) => {
            const wordStr = {}
            used_languages.forEach(lang => {
                if (lang === native_lang) {
                    wordStr[lang] = {str: null, transcription: null}
                } else {
                    wordStr[lang] = {str: null}
                }
            })
            return wordStr;
        });
    const jsonExampleText = JSON.stringify(jsonExample, null, 2)

    let notNativeLangs = "";
    used_languages.forEach((lang,index)=> {
        if(lang !== native_lang){
            notNativeLangs += lang + ".str" + (index < used_languages.length - 1 ? ", " : "")
        }
    })

    const wordType = {};
    used_languages.forEach(lang => {
        if (lang === native_lang) {
            wordType[lang] = {str: null, transcription: null}
        }else {
            wordType[lang] = {str: null}
        }
    })
    const wordTypeText = JSON.stringify(wordType);



    return `Уважаемый Gpt!
 Ответь на это сообщение json-объектом по типу:
 ${jsonExampleText}
 Это фраза "${nativeStr}" на языке ${native_lang}.
 Под ключами ${notNativeLangs} будут храниться переводы строки "${nativeStr}" на соответствующие языки.
 Далее разбей строку "${nativeStr}" на отдельные слова по смыслу и запиши их в массив под ключем "words:[]" обьектами типа ${wordTypeText} с соответствующими переводами слов.
 Обрати внимание, что поле "transcription" имеется только у языка ${native_lang}.
 Дай дополнительные коментарии по поводу особенностей перевода как строки "${nativeStr}" так и ее слов по отдельности.
 Спасибо!`
}

