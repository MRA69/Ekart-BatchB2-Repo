FROM maven:3.9.6-openjdk-24 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:24-jdk-slim
COPY --from=build /target/batchB2-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]