FROM openjdk:latest

FROM gradle:7.4.0-jdk11

WORKDIR /usr/src/app
COPY . ./

RUN gradle jar

ADD build/libs/UnitProject.jar UnitProject.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","build/libs/UnitProject.jar"]