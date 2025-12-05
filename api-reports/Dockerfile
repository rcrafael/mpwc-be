FROM maven:3.9-eclipse-temurin-17 AS build

COPY pom.xml /app/pom.xml
WORKDIR /app
RUN mvn -N install

COPY api-gateway/pom.xml /app/api-gateway/pom.xml
COPY api-user/pom.xml /app/api-user/pom.xml
COPY api-billing/pom.xml /app/api-billing/pom.xml
COPY api-payment/pom.xml /app/api-payment/pom.xml
COPY api-settings/pom.xml /app/api-settings/pom.xml
COPY api-reports/pom.xml /app/api-reports/pom.xml

COPY api-user/src /app/api-user/src
WORKDIR /app/api-user
RUN mvn clean install -DskipTests

COPY api-billing/src /app/api-billing/src
WORKDIR /app/api-billing
RUN mvn clean install -DskipTests

COPY api-payment/src /app/api-payment/src
WORKDIR /app/api-payment
RUN mvn clean package -DskipTests

COPY api-reports/src /app/api-reports/src
WORKDIR /app/api-reports
RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/api-reports/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]