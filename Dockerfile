FROM amazoncorretto:8 AS builder

WORKDIR /app
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src/main ./src/main
RUN sed -i 's/\r//' ./gradlew && ./gradlew bootJar

FROM amazoncorretto:8

WORKDIR /app
COPY --from=builder /app/build/libs/maturi-*.jar app.jar

ENV PROFILE="local-docker"

ENTRYPOINT java -jar app.jar --spring.profiles.active=$PROFILE

# <build 명령어>
# docker build -t springboot-maturi:1.0.0 .
# <run 명령어>
# docker run -p 8080:8080 springboot-maturi:1.0.0