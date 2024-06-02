FROM certbot/certbot:nightly

EXPOSE 80
EXPOSE 443

ARG DOMAIN_NAME
ARG MY_EMAIL
ARG CONTEXT_RELOAD_KEY

ENV DOMAIN_NAME=${DOMAIN_NAME}
ENV MY_EMAIL=${MY_EMAIL}
ENV CONTEXT_RELOAD_KEY=${CONTEXT_RELOAD_KEY}

COPY script.sh /usr/local/bin/script.sh
COPY renew.sh /usr/local/bin/renew.sh
COPY createNewKeyStore.sh /usr/local/bin/createNewKeyStore.sh

RUN chmod +x /usr/local/bin/script.sh
RUN chmod +x /usr/local/bin/renew.sh
RUN chmod +x /usr/local/bin/createNewKeyStore.sh

ENTRYPOINT ["/usr/local/bin/script.sh"]