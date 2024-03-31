let keys = null;


requestingListWords();

/*
//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////*/
function requestingListWords(){
    fetch('/words')
        .then(response => response.json())
        .then(listWords => {
            getKeys(listWords);
            fillTable(listWords);
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}
function getKeys(listWords){
    keys = Object.keys(listWords[0]);
}
function fillTable(listWords){
    const tableListWords = document.getElementById("tableListWords");
    tableListWords.innerHTML = "";

    for (let i = 0; i < listWords.length; i++) {
        const newRow = tableListWords.insertRow(i);

        //заполняем строку
        fillRow(newRow,keys,listWords[i])
        //добавляем слушатель на строку
        addLisenerOnRow(newRow,listWords[i].id);
    }

}
function fillRow(row,keys,word){
    for (let i = 0; i < keys.length; i++) {
        var newCell = row.insertCell(i);
        newCell.innerHTML = word[keys[i]];
    }
}
function addLisenerOnRow(newRow,idValue){
    newRow.onclick = function() {
        openUpdatingModal(idValue);
    };
}