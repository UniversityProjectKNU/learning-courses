FROM maven:3.8.3-jdk-11 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

FROM openjdk:11-jre-slim

WORKDIR /app

COPY --from=builder /app/target/*.jar /app.jar

COPY app-env /app/app-env

ENV SPRING_CONFIG_NAME=app-env
ENV SPRING_CONFIG_LOCATION=/app/app-env/

RUN apt-get update && apt-get install -y default-mysql-client

CMD ["java", "-jar", "/app.jar"]