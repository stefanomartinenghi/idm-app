FROM eclipse-temurin:25.0.4_7-jre-alpine-3.22

WORKDIR /app
COPY image/idm-app.jar /app/idm-app.jar

USER 10001
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/idm-app.jar"]
