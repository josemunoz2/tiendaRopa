# Imagen base oficial con Java 17
FROM eclipse-temurin:17-jdk

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar solo los archivos necesarios
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Dar permisos a mvnw
RUN chmod +x mvnw

# Compilar el proyecto (sin correr los tests para hacerlo más rápido)
RUN ./mvnw clean install -DskipTests

# Exponer el puerto 8080
EXPOSE 8080

# Comando para correr el .jar final
CMD ["java", "-jar", "target/compa-erogay-0.0.1-SNAPSHOT.jar"]
