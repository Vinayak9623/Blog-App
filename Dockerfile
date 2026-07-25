# ==========================
# Stage 1 - Build
# ==========================
FROM maven:3.9.11-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

# ==========================
# Stage 2 - Runtime
# ==========================
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8010

ENTRYPOINT ["java","-jar","app.jar"]

