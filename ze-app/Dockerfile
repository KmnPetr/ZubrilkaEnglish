FROM openjdk:17

RUN mkdir -p /keys/

VOLUME /keys

EXPOSE 28700

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar


#новое
CMD ["java", "-jar", "/app.jar"]