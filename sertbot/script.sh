#!/bin/sh

echo "script.sh startedDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"
# Получить и установить сертификат
certbot certonly --standalone --preferred-challenges http --agree-tos --email kmn.petrichenko@yandex.ru -d 608686.cloud4box.ru

# Добавить задание в cron для еженедельного обновления сертификата
echo "0 0 * * 0 certbot renew --email kmn.petrichenko@yandex.ru -d 608686.cloud4box.ru --non-interactive" | crontab -

# Запустить crond в фоновом режиме
crond -f