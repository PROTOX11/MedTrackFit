# Stage 1: Build the frontend (React app & Tailwind CSS)
FROM node:22-alpine AS frontend-builder
WORKDIR /app

# Install root dependencies (for Tailwind)
COPY package*.json ./
RUN npm ci

# Install frontend dependencies
COPY frontend/package*.json ./frontend/
RUN cd frontend && npm ci

# Copy sources required for compilation
COPY tailwind.config.js ./
COPY src/main/resources/static/css/input.css ./src/main/resources/static/css/input.css
COPY src/main/resources/templates/ ./src/main/resources/templates/
COPY frontend/ ./frontend/

# Compile Tailwind CSS
RUN npm run build:css

# Compile React frontend
RUN cd frontend && npm run build

# Stage 2: Build the Spring Boot application (Maven)
FROM maven:3.9.9-eclipse-temurin-17 AS backend-builder
WORKDIR /app

# Copy pom.xml and download dependencies first (for caching)
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copy all backend source files
COPY src/ ./src/

# Copy the compiled React application and Tailwind CSS from frontend-builder stage
COPY --from=frontend-builder /app/src/main/resources/static/ ./src/main/resources/static/

# Package the application jar
RUN mvn clean package -DskipTests

# Stage 3: Run stage (Lightweight Alpine JRE image, no local DB)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the packaged JAR from the backend-builder stage
COPY --from=backend-builder /app/target/medtrackfit-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]