package com.example.ze_adminandroid.screens.webView

enum class JSCode(val value: String) {
    Get_href_list(
        """
                    (function() {
                        var buttonElement = document.querySelector('.ui.button.floating.dropdown.icon.ml-download-dropdown.ml-right');
                        if (buttonElement) {
                            buttonElement.removeAttribute('disabled');
                            buttonElement.click();
                        }
                    
                    // Находим блок меню
                    var menu = document.querySelector('.right.menu.transition.visible');

                    // Проверяем, что блок меню найден
                    if (menu) {
                        // Находим все элементы с классом "item" внутри блока меню
                        var items = menu.querySelectorAll('.item');
                        // Проверяем, что есть хотя бы два элемента
                        if (items.length >= 2) {
                                // Кликаем на второй элемент
                                return items[0].href+'*****'+items[1].href;
                        } else {
                                return "Недостаточно элементов в меню.";
                        }
                    } else {
                            return "Блок меню не найден.";
                    }
                    })();
                """
    )
}
//<div class="ml-main-panel"><div class="ui header ml-header">apple <span class="ml-transl"> / яблоко, яблоня, чепуха</span></div><div class="ml-sound-group"><div class="ml-item"><i class="gb flag"></i> <span>[ˈæp.l̩]</span> <button class="ui basic button icon primary ml-sound"><i aria-hidden="true" class="volume up icon"></i></button></div><div class="ml-item"><i class="us flag"></i> <span>[ˈæp.l̩]</span> <button class="ui basic button icon primary ml-sound"><i aria-hidden="true" class="volume up icon"></i></button></div></div></div>