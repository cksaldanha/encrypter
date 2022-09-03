FROM eclipse-temurin:8-jdk-jammy AS base
FROM base AS build
WORKDIR /app
COPY src/ src/
WORKDIR /app/src/main/java
RUN mkdir /app/build/ && javac -d /app/build/ $(find . | grep .java)

FROM base AS package
WORKDIR /app
COPY --from=build /app/build/ ./
COPY Manifest.txt .
RUN jar cfm Encrypter.jar Manifest.txt $(find . | grep .class)

FROM eclipse-temurin:8-jre-alpine AS runner 
WORKDIR /app
COPY --from=package /app/Encrypter.jar ./Encrypter.jar
ENTRYPOINT ["java", "-jar", "Encrypter.jar"]
