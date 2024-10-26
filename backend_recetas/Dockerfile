# Usamos una imagen base con Java 17
FROM eclipse-temurin:17-jdk-focal

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo pom.xml primero para aprovechar la caché de Docker
COPY pom.xml .

# Copiar el código fuente
COPY src ./src

# Instalar Maven y construir la aplicación
RUN apt-get update && \
    apt-get install -y maven && \
    mvn clean package -DskipTests && \
    apt-get remove -y maven && \
    apt-get autoremove -y && \
    apt-get clean

# Copiar el archivo JAR de la aplicación desde el host al contenedor Docker
COPY target/backend_recetas-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto 8080 (puerto típico de Spring Boot)
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]