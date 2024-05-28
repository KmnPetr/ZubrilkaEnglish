#!/bin/sh

echo "script.sh started!!!"
echo "DOMAIN_NAME: $DOMAIN_NAME"
# Получить и установить сертификат
certbot certonly --standalone --preferred-challenges http --agree-tos --email kmn.petrichenko@yandex.ru -d $DOMAIN_NAME --non-interactive

# Добавить задание в cron для еженедельного обновления сертификата
echo "0 0 * * 0 certbot renew --email kmn.petrichenko@yandex.ru -d $DOMAIN_NAME --non-interactive" | crontab -

# Запустить crond в фоновом режиме
crond -f