# STAGE 1: Build Spring Boot application
FROM maven:3.8.3-jdk-11 AS builder

WORKDIR /app

#todo setup secure user to execute

# copy file declaring dependencies (better cache)
COPY pom.xml .
# copy project files to /app/src
COPY src ./src

RUN --mount=type=cache,target=/root/.m2 mvn clean install -DskipTests


# STAGE 2: Create the final image
FROM openjdk:11-jre-slim AS app
# copy created .jar file from /app/target/*.jar into root folder
COPY --from=builder /app/target/*.jar /app.jar

# expose port 8080
EXPOSE 8080
CMD ["java", "-jar", "/app.jar"]