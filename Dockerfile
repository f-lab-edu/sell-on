# 1단계: 빌드용 Gradle 이미지 사용
FROM gradle:8.5-jdk17 AS builder

# 캐싱을 위해 필요한 파일만 먼저 복사
COPY build.gradle settings.gradle ./
COPY gradle gradle
RUN gradle build --no-daemon || return 0

# 전체 소스 복사 후 빌드
COPY . .
RUN gradle clean build --no-daemon

# 2단계: 실제 실행용 이미지
FROM openjdk:17-jdk-slim

WORKDIR /app

# 빌드한 jar 복사 (파일명은 아래에서 확인해봐야 함)
COPY --from=builder /home/gradle/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
