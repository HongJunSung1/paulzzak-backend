# 1. OpenJDK 17 이미지 사용
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. Gradle Wrapper 복사 후 실행하여 빌드 수행
COPY . .  # 모든 소스 파일을 컨테이너에 복사
RUN chmod +x ./gradlew  # Gradle Wrapper 실행 권한 부여
RUN ./gradlew build -x test  # 테스트 제외하고 빌드 실행

# 4. 빌드된 WAR 파일을 컨테이너 내부로 복사
COPY build/libs/ESGbbollock-0.0.1-SNAPSHOT.war app.war

# 5. Tomcat 포트 오픈 (Spring Boot 내장 서버 사용)
EXPOSE 8080

# 6. 실행 명령어 설정
CMD ["java", "-jar", "app.war"]