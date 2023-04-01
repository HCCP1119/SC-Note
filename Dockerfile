FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY ./app/cloudnote-gateway-1.0-SNAPSHOT.jar /app/sc-gateway/target/sc-gateway-1.0-SNAPSHOT.jar
COPY ./app/cloudnote-umc-1.0-SNAPSHOT.jar /app/sc-service/sc-umc/target/sc-umc-1.0-SNAPSHOT.jar
COPY ./app/cloudnote-note-1.0-SNAPSHOT.jar /app/sc-service/sc-note/target/sc-note-1.0-SNAPSHOT.jar
COPY ./app/cloudnote-mail-1.0-SNAPSHOT.jar /app/sc-service/sc-mail/target/sc-mail-1.0-SNAPSHOT.jar
COPY ./app/cloudnote-file-1.0-SNAPSHOT.jar /app/sc-service/sc-file/target/sc-file-1.0-SNAPSHOT.jar
