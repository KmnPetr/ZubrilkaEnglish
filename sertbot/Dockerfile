FROM certbot/certbot:nightly

EXPOSE 80
EXPOSE 443

ARG DOMAIN_NAME
ARG MY_EMAIL

VOLUME /etc/letsencrypt/$DOMAIN_NAME

ENV DOMAIN_NAME=${DOMAIN_NAME}
ENV MY_EMAIL=${MY_EMAIL}

COPY script.sh /usr/local/bin/script.sh

RUN chmod +x /usr/local/bin/script.sh

ENTRYPOINT ["/usr/local/bin/script.sh"]