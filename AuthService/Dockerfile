FROM openjdk:17-jdk-alpine
MAINTAINER SIMM0
COPY ./target/AuthService-2.7.3.jar auth-server.jar
ENTRYPOINT ["java","-jar","/auth-server.jar"]