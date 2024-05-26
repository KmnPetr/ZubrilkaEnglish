#!/bin/sh

# Получить и установить сертификат
certbot certonly --standalone --preferred-challenges http --agree-tos --email kmn.petrichenko@yandex.ru -d "$DOMAIN_NAME"

# Добавить задание в cron для еженедельного обновления сертификата
echo "0 0 * * 0 certbot renew --standalone --preferred-challenges http --agree-tos --email kmn.petrichenko@yandex.ru -d $DOMAIN_NAME" | crontab -
crond -f