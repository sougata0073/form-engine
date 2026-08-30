FROM eclipse-temurin:21

WORKDIR /app

ARG SERVER_PORT

COPY ./target/auth-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE "${SERVER_PORT}"

CMD [ "java", "-jar", "app.jar" ]