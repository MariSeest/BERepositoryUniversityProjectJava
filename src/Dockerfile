FROM eclipse-temurin:17-jdk AS build

# Copia tutto il progetto nel container
WORKDIR /app
COPY . .


RUN ./gradlew clean bootJar -x test


FROM eclipse-temurin:17-jre

WORKDIR /app


COPY --from=build /app/build/libs/*.jar app.jar


EXPOSE 8080


ENTRYPOINT ["java","-Dserver.port=$PORT","-jar","app.jar"]
