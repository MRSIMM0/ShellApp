FROM maven:3.8.3-openjdk-17-slim as build
MAINTAINER SIMM0
COPY . .
RUN mvn -DskipTests -f pom.xml clean package

FROM openjdk:17-jdk-alpine
MAINTAINER SIMM0
COPY --from=build ./target/ShellServce-2.7.3.jar shell-service.jar
ENTRYPOINT ["java","-jar","/shell-service.jar"]