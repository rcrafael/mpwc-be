dockerfile# Multi-stage build
FROM maven:3.9-eclipse-temurin-17 AS build

# Copy parent pom
COPY pom.xml /app/pom.xml
# Copy all module poms first (for better caching)
COPY api-gateway/pom.xml /app/api-gateway/pom.xml
COPY api-user/pom.xml /app/api-user/pom.xml

# Download dependencies
WORKDIR /app
RUN mvn dependency:go-offline -B

# Copy source code
COPY api-gateway/src /app/api-gateway/src

# Build the application
WORKDIR /app/api-gateway
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/api-gateway/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]