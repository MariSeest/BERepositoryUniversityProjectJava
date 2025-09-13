FROM eclipse-temurin:17-jdk AS build
WORKDIR /app


COPY gradlew gradlew.bat ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./


RUN chmod +x ./gradlew


RUN ./gradlew --no-daemon dependencies || true

# Sorgenti
COPY src ./src


RUN ./gradlew --no-daemon clean bootJar -x test \
 && ls -l build/libs


FROM eclipse-temurin:17-jre
WORKDIR /app


COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENV JAVA_OPTS=""

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
