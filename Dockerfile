FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /workspace
COPY pom.xml .
RUN mvn -B -ntp dependency:go-offline
COPY src ./src
RUN mvn -B -ntp verify

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=build /workspace/target/idm-app-1.0.0-SNAPSHOT.jar /app/idm-app.jar

USER 10001
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/idm-app.jar"]
