FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/inventorysyncservice-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9093

ENTRYPOINT ["java", "-jar", "app.jar"]

HEALTHCHECK --interval=10s --timeout=3s --start-period=20s \
  CMD wget -qO- http://localhost:9093/api/inventory-events/ping || exit 1