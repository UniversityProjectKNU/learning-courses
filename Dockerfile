FROM eclipse-temurin:11
ADD target/*.jar learning-courses.jar
ENTRYPOINT ["java", "-jar", "learning-courses.jar"]