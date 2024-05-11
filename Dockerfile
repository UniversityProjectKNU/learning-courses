# STAGE 1: Build Spring Boot application
FROM maven:3.9.6-amazoncorretto-21 AS builder

WORKDIR /app

#todo setup secure user to execute

# copy file declaring dependencies (better cache)
COPY pom.xml .
# copy project files to /app/src
COPY src ./src

RUN --mount=type=cache,target=/root/.m2 mvn clean install -DskipTests -P dev,!cloud


# STAGE 2: Create the final image
FROM openjdk:21-slim AS app
# copy created .jar file from /app/target/*.jar into root folder
COPY --from=builder /app/target/*.jar /app.jar

# expose port 8080
EXPOSE 8080
CMD ["java", "-jar", "/app.jar"]