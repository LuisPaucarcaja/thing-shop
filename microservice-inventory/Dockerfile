FROM openjdk:17-jdk-alpine
LABEL authors="Luis"
COPY target/microservice-inventory-0.0.1-SNAPSHOT.jar service.jar
ENTRYPOINT ["java" , "-jar" , "/service.jar"]