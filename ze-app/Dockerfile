FROM openjdk:17

VOLUME /keys
WORKDIR /

RUN mkdir -p /keys/

EXPOSE 28700
EXPOSE 8443

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

#новое
CMD ["java", "-jar", "/app.jar"]