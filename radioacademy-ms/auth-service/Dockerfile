# Usamos una imagen ligera de Java 17
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el JAR generado por Maven
COPY target/*.jar app.jar

# Exponemos el puerto 
EXPOSE 8081 

# Comando para arrancar
ENTRYPOINT ["java", "-jar", "app.jar"]