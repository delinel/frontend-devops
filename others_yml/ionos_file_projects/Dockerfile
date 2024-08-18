# Mon image de base
FROM nginx:latest

# Labels
LABEL version=v24.8.3
LABEL owner=delinel

# Install basic tools
RUN apt-get update -y && apt-get install -y iputils-ping vim
RUN mkdir -p /etc/nginx/sites-available
RUN mkdir -p /etc/nginx/sites-enable


# Define and create user app
ARG APP_USER=admin
ARG SHELL_USER=/bin/bash
RUN useradd -rm -d /home/${APP_USER} -s ${SHELL_USER} -G sudo -u 901 ${APP_USER}

# Copier le snaphot.jar dans le repertoire de travail et le renommer en code-frontend.jar
#RUN mv /etc/nginx/conf.d/default.conf /etc/nginx/conf.d/default.conf.disabled
#COPY nginx_quarkus-frt.conf /etc/nginx/conf.d/
#RUN nginx -s reload
