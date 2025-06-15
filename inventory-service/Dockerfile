# Temel imaj olarak OpenJDK 17 kullanıyorum, sen Java versiyonuna göre değiştirebilirsin
FROM openjdk:17-jdk-slim


# Çalışma dizini oluştur ve ayarla
WORKDIR /app

# Maven ile build ettiğin jar dosyasını kopyala (ör: target/inventory-service-1.0-SNAPSHOT.jar)
COPY target/inventory-service-1.0-SNAPSHOT.jar app.jar

# 8080 portunu expose et (gerekirse grpc için farklı port varsa ekle)
EXPOSE 8080 9090

# Uygulamayı başlat
ENTRYPOINT ["java", "-jar", "app.jar"]
