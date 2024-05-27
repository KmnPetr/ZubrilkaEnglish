FROM certbot/certbot:nightly

#ARG DOMAIN_NAME

EXPOSE 80
EXPOSE 443

# Копирование скрипта в контейнер
COPY script.sh /usr/local/bin/script.sh

# Делаем скрипт исполняемым
RUN chmod +x /usr/local/bin/script.sh

# Выполнение скрипта
#CMD ["/usr/local/bin/script.sh"]