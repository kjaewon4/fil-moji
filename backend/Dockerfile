# OpenJDK 17 기반 이미지 사용
FROM openjdk:17-jdk-slim

# JAR 파일이 복사될 컨테이너 내부 경로 설정
ARG JAR_FILE=build/libs/*.jar

# JAR 파일을 컨테이너에 복사
COPY ${JAR_FILE} app.jar

# 애플리케이션이 사용할 포트 지정 (Spring Boot 기본 포트)
EXPOSE 8080

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
