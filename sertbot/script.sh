#!/bin/sh

echo "script.sh started!!!"

# Получить и установить сертификат
certbot certonly --standalone --preferred-challenges http --agree-tos --email $MY_EMAIL -d $DOMAIN_NAME --non-interactive

#создаем файл с ключами который может прочитать сервер java
rm /etc/letsencrypt/live/$DOMAIN_NAME/keystore.p12 || true
openssl pkcs12 -export -in /etc/letsencrypt/live/$DOMAIN_NAME/fullchain.pem -inkey /etc/letsencrypt/live/$DOMAIN_NAME/privkey.pem -out /etc/letsencrypt/live/$DOMAIN_NAME/keystore.p12 -name tomcat -CAfile /etc/letsencrypt/live/$DOMAIN_NAME/fullchain.pem -caname root -passout pass:password

# Добавить задание в cron для еженедельного обновления сертификата
echo "0 0 * * 0 /usr/local/bin/renew.sh" | crontab -

# Запустить crond в фоновом режиме чтоб контейнер не завершался
crond -f