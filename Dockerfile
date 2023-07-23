#FROM eclipse-temurin:11
#ADD target/*.jar learning-courses.jar
#ENV SPRING_PROFILES_ACTIVE=prod
#ENV SPRING_CONFIG_LOCATION=classpath:/app-env/
#ENTRYPOINT ["java", "-jar", "learning-courses.jar"]


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
COPY target/*.jar /app.jar

# Копіюємо файли змінних оточення для Spring Boot застосунку та бази даних
COPY app-env /app-env

# Задаємо змінні оточення для Spring Boot застосунку
ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_CONFIG_LOCATION=classpath:/app-env/

# Задаємо змінні оточення для налаштування бази даних
ENV MYSQL_DATABASE=courses
ENV MYSQL_USER=root
ENV MYSQL_PASSWORD=root
ENV MYSQL_ROOT_PASSWORD=root

# Встановлюємо MySQL-клієнт
RUN apt-get update && apt-get install -y default-mysql-client

# Виконуємо команду для запуску додатку
CMD ["java", "-jar", "/app.jar"]