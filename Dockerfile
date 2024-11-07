# 1. Java 11을 베이스 이미지로 사용
FROM openjdk:11-jre-slim

# 2. 애플리케이션 디렉토리 설정
WORKDIR /app

# 3. 빌드된 JAR 파일을 컨테이너로 복사
COPY target/yogijogi-0.0.1-SNAPSHOT.jar app.jar

# 4. 포트 설정 (Spring Boot의 기본 포트)
EXPOSE 8080

# 5. JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]