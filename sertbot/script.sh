#!/bin/sh

echo "script.sh started!!!"

# Получить и установить сертификат
certbot certonly --standalone --preferred-challenges http --agree-tos --email $MY_EMAIL -d $DOMAIN_NAME --non-interactive

# Добавить задание в cron для еженедельного обновления сертификата
echo "0 0 * * 0 certbot renew --email $MY_EMAIL -d $DOMAIN_NAME --non-interactive" | crontab -

# Запустить crond в фоновом режиме чтоб контейнер не завершался
crond -f