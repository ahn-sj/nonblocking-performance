# Dockerfile (webflux-gateway)

# 1단계: 빌드용
FROM amazoncorretto:21 as builder

WORKDIR /app
COPY . .
RUN ./gradlew clean bootJar

# 2단계: 실행용
FROM amazoncorretto:21

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
