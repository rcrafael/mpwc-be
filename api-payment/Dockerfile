FROM maven:3.9-eclipse-temurin-17 AS build

COPY pom.xml /app/pom.xml
COPY api-gateway/pom.xml /app/api-gateway/pom.xml
COPY api-user/pom.xml /app/api-user/pom.xml
COPY api-billing/pom.xml /app/api-billing/pom.xml
COPY api-payment/pom.xml /app/api-payment/pom.xml
COPY api-settings/pom.xml /app/api-settings/pom.xml

WORKDIR /app
RUN mvn dependency:go-offline -B

COPY api-payment/src /app/api-payment/src

WORKDIR /app/api-payment
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/api-payment/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]