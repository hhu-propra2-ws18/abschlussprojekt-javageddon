FROM openjdk:8-jdk-alpine AS build
WORKDIR /app
COPY . ./
RUN ./gradlew --no-daemon --stacktrace clean bootRun





