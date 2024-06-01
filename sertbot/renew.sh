#!/bin/sh

echo "renew.sh started!!!"

certbot renew --email $MY_EMAIL -d $DOMAIN_NAME --non-interactive

openssl pkcs12 -export -in /etc/letsencrypt/live/$DOMAIN_NAME/fullchain.pem -inkey /etc/letsencrypt/live/$DOMAIN_NAME/privkey.pem -out /etc/letsencrypt/live/$DOMAIN_NAME/keystore.p12 -name tomcat -CAfile /etc/letsencrypt/live/$DOMAIN_NAME/fullchain.pem -caname root -passout pass:password