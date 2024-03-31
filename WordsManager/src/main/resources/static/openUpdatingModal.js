const modalWindow = document.getElementById('modal');
const title_modal = document.getElementById('title_modal');
const form_modal = document.getElementById('form_modal');
const idWord = document.createElement('h3');
const buttons_modal = document.getElementById('buttons_modal');

function openUpdatingModal(idValue){
    modalWindow.classList.add('open');

    const idValueConst = idValue; /*чтобы это значение денамически не изменялось*/
    idWord_This = idValueConst;
    fillModalWindow(idValueConst);
}

function fillModalWindow(idValueConst){
    fetchWordById(idValueConst);
}
function fetchWordById(id){
    fetch('/words/'+id)
        .then(response => response.json())
        .then(word => {
            fillModalBox(word);
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}
function fillModalBox(word){
    title_modal.innerText = 'Updating the word!';

    form_modal.innerHTML = "";

    idWord.id = 'idWord';
    idWord.innerText = 'Id: '+word.id;
    form_modal.appendChild(idWord);

    let i = 1;
    for (; i < keys.length; i++) {
        const input = document.createElement('textarea');
        input.type = 'text';
        input.placeholder = keys[i];
        input.id = keys[i]+'Word';
        if (word[keys[i]]!=null){
            input.value = word[keys[i]];
        }
        const defaultValue = word[keys[i]];
        input.addEventListener('input', function() {
            if (input.value !== defaultValue) {
                input.style.color = "blue";
            } else {
                input.style.color = "black";
            }
        });
        form_modal.appendChild(input);
    }

    buttons_modal.innerHTML = "";
    const update_button = document.createElement('button');
    update_button.innerText = 'Send';
    const close_button = document.createElement('button');
    close_button.innerText = 'Close';
    buttons_modal.appendChild(update_button);
    buttons_modal.appendChild(close_button);
    update_button.addEventListener('click',function (){
        updateWord(word.id);
        closeModal();
    })
    close_button.addEventListener('click',function (){
        closeModal();
    });
}
/**
 * эти два метода скрывают модальное окно если клик был на серой области
 */
document.querySelector('#modal .modal_box').addEventListener('click',event => {
    event._isClickWithInModal = true;
});
document.getElementById('modal').addEventListener('click',event=>{
    if(event._isClickWithInModal) return;
    closeModal();
});

function closeModal(){
    form_modal.innerHTML = "";
    modalWindow.classList.remove('open');
    buttons_modal.innerHTML = "";
}

function updateWord(wordId){
    const wordMap = new Map();
    wordMap.set('id',wordId);
    let i = 1;
    for (; i < keys.length; i++) {
        const textarea = document.getElementById(keys[i] + 'Word');
        const value = textarea.value;
        wordMap.set(keys[i],value);
    }


    const wordObject = Object.fromEntries(wordMap);

// Отправляем объект в виде JSON-строки
    fetch('/words/update', {
        method: 'POST',
        body: JSON.stringify(wordObject),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        console.log('Статус запроса: ' + response.status);
    }).catch(error => {
        console.error('Произошла ошибка: ', error);
    });
}
