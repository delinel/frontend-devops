# Mon image de base
FROM maven:3-openjdk-17

# Labels
LABEL version=v24.8.1
LABEL owner=delinel

# Répertoire de travail
WORKDIR /myfront-app

# Copier le snaphot.jar dans le repertoire de travail et le renommer en code-frontend.jar
#COPY /projects/github/depot/mkutano-frontend/  /myfront-app/frontend
COPY -R ./frontend-devops  /myfront-app

RUN mvn clean package

# Exposer le port de l'application
EXPOSE 8080

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "target/quarkus-app/quarkus-run.jar"]