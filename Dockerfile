FROM openjdk:17-jdk-slim
WORKDIR /app
COPY main-application/target/main-application-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
