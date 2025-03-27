# Этап 1: Сборка приложения
FROM openjdk:18-jdk as build

WORKDIR /usr/src/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

COPY src src

RUN chmod +x mvnw
RUN ./mvnw package -DskipTests
RUN ls /usr/src/app/target/

FROM tomcat:9-jdk11-openjdk

RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /usr/src/app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8081

CMD ["catalina.sh", "run"]
