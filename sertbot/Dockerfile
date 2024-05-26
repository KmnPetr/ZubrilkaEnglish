FROM certbot/certbot

ARG DOMAIN_NAME

EXPOSE 80

# Получить и установить сертификат в первый раз
RUN certbot certonly --standalone --preferred-challenges http --agree-tos --email kmn.petrichenko@yandex.ru -d $DOMAIN_NAME
# Добавить задание в cron для еженедельного обновления сертификата
RUN echo "0 0 * * 0 certbot renew --standalone --preferred-challenges http --agree-tos --email kmn.petrichenko@yandex.ru -d $DOMAIN_NAME" | crontab -
#запускаем хрон чтобы контейнер не остановился

# Запуск crond в фоновом режиме при запуске контейнера
CMD ["crond", "-f"]