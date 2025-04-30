FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre
RUN useradd --system --uid 10001 ats
WORKDIR /app
COPY --from=build /workspace/target/ats-api-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir /app/uploads && chown -R ats:ats /app
USER ats
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

