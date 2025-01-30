import { copyToClipboard } from "./copyToClipboard";

export const openAiGptRequest =(nativeStr,nativeKey)=>{
    //скопирует запрос строку в буфер
    copyToClipboard(getRuText(nativeStr))
    //перенаправит в чат gpt и задаст ему вопрос
    redirectToChatGPT(getRuText(nativeStr,nativeKey));
}



function redirectToChatGPT(query) {
    // Формируем URL с переданным запросом
    const baseUrl = "https://chat.openai.com/";
    const encodedQuery = encodeURIComponent(query);
    const finalUrl = `${baseUrl}?q=${encodedQuery}`;

    // Открываем URL в новой вкладке
    window.open(finalUrl, '_blank');
}


const getRuText = (nativeStr,nativeKey) => `Уважаемый Gpt!
 Ответь на это сообщение json-объектом в котором будут 3 ключа "cn","en","ru".
 Под ключем ${nativeKey} будет строка - "${nativeStr}". 
 Под остальными ключами будут переводы этой строки на соответствующие языки. 
 Далее разбей строку "${nativeStr}" на отдельные слова по смыслу и запиши их в массив под ключем "words" обьектами типа "{"cn","en","ru"}" 
 с соответствующими переводами.
 Дай дополнительные коментарии по поводу особенностей перевода как строки "${nativeStr}" так и ее слов по отдельности. 
 Спасибо!`