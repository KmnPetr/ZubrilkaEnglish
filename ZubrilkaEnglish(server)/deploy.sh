#!/bin/bash

remote_ip="46.8.19.22"
echo "Деплой будет осуществляться на хост " $remote_ip "."
echo "При необходимости изменить ip для деплоя измени в .sh значение переменной remote_ip"
echo


echo "//////////////////Maven сборка.////////////////////////////////////////////////////////"
mvn clean package

echo "//////////////////Сборка образа 'kmnpetr/project'//////////////////////////////////////"
docker build -t kmnpetr/ze .

echo "//////////////////Push образа 'kmnpetr/project' на dockerhub///////////////////////////"
docker push kmnpetr/ze

echo "//////////////////Подготовка сервера./////////////////////////////////////////"
ssh  root@$remote_ip  << EOF
docker compose -f /home/ze/docker-compose.yml down

mkdir /home/ze/
EOF

echo "//////////////////Копирование файла docker-compose.yml///////////////////////////////////////////////////"
scp ./docker-compose.yml root@$remote_ip:/home/ze

echo "//////////////////Запуск docker-compose.yml.///////////////////////////////////////////"
ssh root@$remote_ip << EOF
docker compose -f /home/ze/docker-compose.yml up -d
EOF
echo "//////////////////docker-compose.yml запущен.//////////////////////////////////////////"