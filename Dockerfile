# Stage 1: Build Spring Boot application
FROM maven:3.8.3-jdk-11 AS builder

WORKDIR /app

# Копіюємо файли для залежностей та збираємо проект
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

# Stage 2: Create the final image
FROM openjdk:11-jre-slim

# Копіюємо JAR файл з першого стейджа
COPY --from=builder /app/target/*.jar /app.jar

# Встановлюємо MySQL-клієнт
RUN apt-get update && apt-get install -y default-mysql-client

# Виконуємо команду для запуску додатку
EXPOSE 8080
CMD ["java", "-jar", "/app.jar"]