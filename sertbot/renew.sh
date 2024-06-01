#!/bin/sh
#скрипт вызывается хроном переодически обновляя сертификат ssl если это необходимо
# а также создавая новый файл с ключами keystore.p12 который может прочитать сервер java


echo "renew.sh started!!!"

certbot renew --non-interactive --deploy-hook "/usr/local/bin/createNewKeyStore.sh"