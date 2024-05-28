FROM certbot/certbot:nightly

EXPOSE 80
EXPOSE 443

ARG DOMAIN_NAME
COPY script.sh /usr/local/bin/script.sh

RUN echo "Hello from Dockerfile"
RUN echo "DOMAIN_NAME: $DOMAIN_NAME"

RUN chmod +x /usr/local/bin/script.sh

ENTRYPOINT ["/usr/local/bin/script.sh"]