# Usamos una imagen oficial de mysql
#FROM mysql/mysql-server:latest

# Define environment variables 
#ENV MYSQL_ROOT_PASSWORD=p4ssw0rd
#ENV MYSQL_DATABASE=db_recetas_web
#ENV MYSQL_USER=user_recetas_web
#ENV MYSQL_PASSWORD=p4ssw0rd_recetas_web

# Usamos una imagen base con Java 17
FROM eclipse-temurin:17-jdk-focal AS buildstage 

RUN apt-get update && apt-get install -y maven

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo pom.xml primero para aprovechar la caché de Docker
COPY pom.xml .

# Copiar el código fuente
COPY src /app/src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-focal

# Copia el archivo jar al contenedor
COPY --from=buildstage /app/target/backend_recetas-0.0.1-SNAPSHOT.jar /app/app.jar

# Exponer el puerto del servicio
EXPOSE 8081

# Ejecutar el servicio
ENTRYPOINT [ "java", "-jar","/app/app.jar" ]
