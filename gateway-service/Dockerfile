# Build context is the repo root (multiple modules share the parent pom.xml).
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .
COPY user-service/pom.xml user-service/pom.xml
COPY product-service/pom.xml product-service/pom.xml
COPY order-service/pom.xml order-service/pom.xml
COPY payment-service/pom.xml payment-service/pom.xml
COPY cart-service/pom.xml cart-service/pom.xml
COPY gateway-service/pom.xml gateway-service/pom.xml
RUN mvn -pl gateway-service dependency:go-offline -B

COPY gateway-service/src gateway-service/src
RUN mvn -pl gateway-service package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /build/gateway-service/target/*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
