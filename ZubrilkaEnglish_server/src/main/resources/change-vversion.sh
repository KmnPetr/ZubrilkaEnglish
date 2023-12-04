#!/bin/bash

# Считываем текущее значение app-version из файла
current_version=$(grep 'app-version=' application.properties | sed 's/app-version=//')

# Используем awk для увеличения последней цифры на 1
new_version=$(echo $current_version | awk -F. '{$NF = $NF + 1} 1' OFS=.)

# Заменяем значение в файле
sed -i "s/app-version=$current_version/app-version=$new_version/" application.properties