# OpenJDK 17을 사용하여 Java 환경 설정
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 WAR 파일을 컨테이너 내부로 복사
COPY build/libs/ESGbbollock-0.0.1-SNAPSHOT.war app.war

# Tomcat 포트 오픈 (Spring Boot 내장 서버 사용)
EXPOSE 8080

# 실행 명령어 설정
CMD ["java", "-jar", "app.war"]