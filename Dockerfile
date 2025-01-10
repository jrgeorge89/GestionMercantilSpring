# Utilizar una imagen base de OpenJDK
FROM openjdk:17-jdk-alpine

# Configurar la variable de entorno del JAR
ARG JAR_FILE=target/*.jar

# Copiar el JAR en el contenedor
COPY ${JAR_FILE} app.jar

# Exponer el puerto en el que la aplicación se ejecutará
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","/app.jar"]