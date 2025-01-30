
export const copyToClipboard=(text)=> {
    navigator.clipboard.writeText(text).catch(err => {
        console.error("Ошибка при копировании текста:", err);
    });
}