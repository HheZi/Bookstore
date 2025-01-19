FROM eclipse-temurin:18-jdk-jammy as build

COPY src src

COPY gradlew gradlew.bat settings.gradle build.gradle ./

COPY gradle gradle

RUN ./gradlew build



FROM eclipse-temurin:18-jre-jammy

WORKDIR /home/app

ARG JAR_FILE=./build/libs/*.jar

COPY --from=build $JAR_FILE app.jar

COPY images images

ENTRYPOINT [ "java", "-jar", "app.jar"]

