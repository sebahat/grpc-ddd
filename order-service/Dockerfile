# Base image olarak OpenJDK 19 kullanıyoruz
FROM openjdk:17-jdk-slim

# Çalışma dizini oluştur
WORKDIR /app

# Maven build sonrası jar dosyasını kopyala
COPY target/order-service-1.0-SNAPSHOT.jar app.jar

EXPOSE 9092

# Uygulamayı çalıştır
ENTRYPOINT ["java", "-jar", "app.jar"]

