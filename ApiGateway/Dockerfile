FROM openjdk:17-jdk-alpine
MAINTAINER SIMM0
COPY ./target/ApiGateway-2.7.3.jar gateway.jar
ENTRYPOINT ["java","-jar","/gateway.jar"]