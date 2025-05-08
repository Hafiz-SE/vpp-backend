# ---- Stage 1: Build with Maven ----
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml and download dependencies first (cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the full source code
COPY src ./src

# Package the application (skip tests to speed up)
RUN mvn clean package -DskipTests

# ---- Stage 2: Runtime with JRE only ----
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose app port (optional)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
