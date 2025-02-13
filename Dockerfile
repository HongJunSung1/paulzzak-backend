# 1. OpenJDK 17 이미지 사용
FROM openjdk:17-jdk-slim AS builder

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 소스 코드 복사
COPY . .

# 4. Gradle Wrapper 실행 권한 부여
RUN chmod +x ./gradlew

# 5. Gradle 빌드 수행 (테스트 제외)
RUN ./gradlew build -x test

# 6. 런타임을 위한 가벼운 JDK 이미지 사용
FROM openjdk:17-jdk-slim

# 7. 작업 디렉토리 설정
WORKDIR /app

# 8. 빌드된 WAR 파일을 복사
COPY --from=builder /app/build/libs/ESGbbollock-0.0.1-SNAPSHOT.war app.war

# 9. Tomcat 포트 오픈 (Spring Boot 내장 서버 사용)
EXPOSE 8080

# 10. 실행 명령어 설정
CMD ["java", "-jar", "app.war"]