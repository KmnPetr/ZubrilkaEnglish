#!/bin/sh
echo "createNewKeyStore.sh started!!!"
#создаем файл с ключами который может прочитать сервер java
rm /etc/letsencrypt/live/$DOMAIN_NAME/keystore.p12 || true
openssl pkcs12 -export -in /etc/letsencrypt/live/$DOMAIN_NAME/fullchain.pem -inkey /etc/letsencrypt/live/$DOMAIN_NAME/privkey.pem -out /etc/letsencrypt/live/$DOMAIN_NAME/keystore.p12 -name tomcat -CAfile /etc/letsencrypt/live/$DOMAIN_NAME/fullchain.pem -caname root -passout pass:password
echo "new file key store was created"