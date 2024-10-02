FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]



# 1. docker-compose up -d

# Démarre tous les services définis dans le fichier docker-compose.yml en mode détaché (arrière-plan). Elle gère plusieurs conteneurs à la fois.


# 2. docker build -t abc .

# Construit une image Docker à partir du Dockerfile dans le répertoire courant. Elle crée une seule image nommée "abc" mais ne démarre pas de conteneur.

# La première est utilisée pour orchestrer plusieurs conteneurs, tandis que la seconde est pour construire une image individuelle.