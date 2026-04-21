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


COPY --from=build /app/target/inventory-service-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080 9090


HEALTHCHECK --interval=10s --timeout=3s --start-period=20s \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]