FROM openjdk:17-jdk-alpine
COPY globetrotters-0.0.1-SNAPSHOT.jar myapp.jar
EXPOSE 3306 8080
ENTRYPOINT ["java", "-jar", "myapp.jar"]