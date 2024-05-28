FROM certbot/certbot:nightly

EXPOSE 80
EXPOSE 443

COPY script.sh /usr/local/bin/script.sh

RUN echo "Hello from Dockerfile"

RUN chmod +x /usr/local/bin/script.sh

CMD ["/usr/local/bin/script.sh"]