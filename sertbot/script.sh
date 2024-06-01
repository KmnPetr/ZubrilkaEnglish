#!/bin/sh
#скрипт вызывает создание нового сертификата ssl а также удаляет старый файл keystore.p12
#предназначенный для сервера java

echo "script.sh started!!!"

# Получить и установить сертификат
certbot certonly --standalone --preferred-challenges http --agree-tos --email $MY_EMAIL -d $DOMAIN_NAME --non-interactive

/usr/local/bin/createNewKeyStore.sh
# Добавить задание в cron для еженедельного обновления сертификата
echo "0 0 * * 0 /usr/local/bin/renew.sh" | crontab -

# Запустить crond в фоновом режиме чтоб контейнер не завершался
crond -f