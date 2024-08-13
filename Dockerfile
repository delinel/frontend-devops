# Mon image de base
FROM maven:3-openjdk-17

# Labels
LABEL version=v24.8.3
LABEL owner=delinel

# Répertoire de travail
WORKDIR /myfront-app

# Define and create user app
ARG APP_USER=appdev
ARG SHELL_USER=/bin/bash
RUN useradd -rm -d /home/${APP_USER} -s ${SHELL_USER} -G wheel -u 1001 ${APP_USER}

# Add the project in the folder
USER ${APP_USER}
ADD frontend-devops  /myfront-app

RUN mvn package

# Exposer le port de l'application
EXPOSE 8080

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "target/quarkus-app/quarkus-run.jar"]
