FROM openjdk:17-jdk-alpine
LABEL authors="Luis Paucarcaja"
COPY target/product-service-0.0.1-SNAPSHOT.jar service.jar
ENTRYPOINT ["java" , "-jar" , "/service.jar"]