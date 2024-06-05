
FROM victoriametrics/victoria-metrics:v1.101.0

# Копируем файл конфигурации в контейнер
COPY /victoriametrics/promscrape.yaml /promscrape.yaml

# Устанавливаем команду запуска с указанием пути к файлу конфигурации
ENTRYPOINT ["/victoria-metrics-prod", "-promscrape.config=/promscrape.yaml"]