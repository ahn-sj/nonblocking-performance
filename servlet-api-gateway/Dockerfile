# 1단계: Build Stage
FROM amazoncorretto:21 AS builder

WORKDIR /app

# Gradle Wrapper 복사 및 캐싱
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle

RUN ./gradlew dependencies --no-daemon || return 0

# 전체 복사 후 빌드
COPY . .

RUN ./gradlew clean bootJar --no-daemon

# 2단계: Runtime Stage
FROM amazoncorretto:21 AS runner

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
