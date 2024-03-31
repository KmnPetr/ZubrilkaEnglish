(function (){
    const runBackupButton = document.getElementById('runBackup')

    runBackupButton.addEventListener('click',function (){
        console.log('отправлен запрос на бэкап')
        fetch('/backup/save-words', {
            method: 'POST',
            // body: JSON.stringify(wordObject),
            // headers: {
            //     'Content-Type': 'application/json'
            // }
        }).then(response => {
            console.log('Статус запроса: ' + response.status);
        }).catch(error => {
            console.error('Произошла ошибка: ', error);
        });
    })
})();