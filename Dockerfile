# Use a base image with Java and Maven to build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Copy the source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn package

# Create a lightweight container to run the Spring Boot application
FROM openjdk:17-alpine
WORKDIR /app

# Copy the JAR file built in the previous stage to this container
COPY --from=build /app/target/animelz-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port the application runs on
EXPOSE 8085

# Command to run the application
CMD ["java", "-jar", "app.jar"]