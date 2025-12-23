# ---------- BUILD STAGE ----------
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copy all project (root)
COPY .. .

# gradlew from root
RUN chmod +x gradlew

# Build backdend only
RUN ./gradlew :backend:bootJar -x test

# ---------- RUN STAGE ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*

# Copy JAR backend
COPY --from=build /app/backend/build/libs/*.jar app.jar

# wait-script in root
COPY --from=build /app/wait-for-postgres.sh wait-for-postgres.sh
RUN chmod +x wait-for-postgres.sh

EXPOSE 8080

ENTRYPOINT ["./wait-for-postgres.sh", "floqdrive-postgres", "java", "-jar", "app.jar"]
