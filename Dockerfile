# Mon image de base
FROM maven:3-openjdk-17

# Labels
LABEL version=v24.8.2
LABEL owner=delinel

# Répertoire de travail
WORKDIR /myfront-app

# Copie du projet dans le repetoire de travail
COPY ./frontend-devops  /myfront-app

RUN mvn clean package

# Exposer le port de l'application
EXPOSE 8080

# Démarrage de l'application
ENTRYPOINT ["java", "-jar", "./target/quarkus-app/quarkus-run.jar"]