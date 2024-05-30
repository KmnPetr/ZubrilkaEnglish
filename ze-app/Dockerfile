FROM openjdk:17

VOLUME /app
VOLUME /keys

EXPOSE 28700

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

RUN mkdir -p /keys/

#новое
CMD ["java", "-jar", "/app.jar"]