FROM eclipse-temurin:21-jdk-alpine AS builder

### MOVE TO THE FOLDER "/app" ###
WORKDIR /app

### COPY BUILD CONFIG FILES TO THE CONTAINER ###
COPY pom.xml .
COPY ./mvn ./mvn
COPY mvnw* .

### DOWNLOAD DEPENDENCIES ###
RUN ./mvnw dependency:go-offline

### COPY CODE TO THE CONTAINER ###
COPY src/ ./src

### COMPILE THE CODE ###
RUN ./mvnw clean package -DskipTests

#### SWITCH TO UNPRIVILEGED USER ###
## Add unprivileged user
#RUN adduser appUser
## Switch to created user
#USER appUser
