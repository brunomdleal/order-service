FROM eclipse-temurin:21-jdk-alpine AS builder

### MOVE TO THE FOLDER "/app" ###
WORKDIR /app

### COPY BUILD CONFIG FILES TO THE CONTAINER ###
COPY pom.xml .
COPY .mvn/ .mvn
COPY mvnw* .

### DOWNLOAD DEPENDENCIES ###
RUN ./mvnw dependency:go-offline

### COPY CODE TO THE CONTAINER ###
COPY src/ ./src

### COMPILE THE CODE ###
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine AS runner

### MOVE TO THE FOLDER "/app" ###
WORKDIR /app

### ADD UNPRIVILEGED USER WITHOUT PASSWORD (-D)###
RUN adduser appUser -D

### COPY JAR FILE FROM BUILDER ###
COPY --chown=appUser:appUser --from=builder /app/target/*.jar ./app.jar

### SWITCH TO ADDED USER ###
USER appUser

### SET ENTRYPOINT ###
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]