# ======================
# BUILD STAGE
# ======================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests


# ======================
# RUNTIME STAGE
# ======================
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Fixed JAR name
COPY --from=build /app/target/auth-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9094

ENTRYPOINT ["java", "-jar", "app.jar"]

HEALTHCHECK --interval=10s --timeout=3s --start-period=20s \
  CMD wget -qO- http://localhost:9094/actuator/health || exit 1
