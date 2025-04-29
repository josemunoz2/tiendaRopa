# Imagen base oficial con Java 17
FROM eclipse-temurin:17-jdk

# Directorio dentro del contenedor
WORKDIR /app

# Copiamos todo el proyecto
COPY . .

# Damos permiso al mvnw
RUN chmod +x mvnw

# Construimos la app
RUN ./mvnw clean install

# Exponemos el puerto 8080
EXPOSE 8080

# Comando para arrancar la app
CMD ["java", "-jar", "target/*.jar"]
