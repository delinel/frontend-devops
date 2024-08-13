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
ADD .  /myfront-app

# Buils the jar file with maven
USER root
RUN mvn package

# Set permissions for user app
RUN chown -R ${APP_USER}:${APP_USER} /myfront-app

# Exposer le port de l'application
EXPOSE 8080

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "target/code-frontend-1.0.0-SNAPSHOT-runner.jar"]
