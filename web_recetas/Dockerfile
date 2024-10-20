# Usar una imagen base de JDK para construir la aplicación
# FROM openjdk:17-jdk-alpine
# Usa una imagen base con Java 17
FROM eclipse-temurin:17-jdk-focal

# Crear un directorio en la imagen Docker donde se alojará la aplicación
WORKDIR /app

# Copiar el archivo JAR de la aplicación desde el host al contenedor Docker
COPY target/web_recetas-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto 8080 (puerto típico de Spring Boot)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]