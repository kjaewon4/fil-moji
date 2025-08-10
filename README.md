# FilMoji- NFT 기반 티켓 암표 방지 서비스

![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

![Java](https://img.shields.io/badge/Java-007396?style=flat-square&logo=java&logoColor=white)
![Python](https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=python&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=javascript&logoColor=black)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=flat-square&logo=css3&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=flat-square&logo=thymeleaf&logoColor=white)
![FastAPI](https://img.shields.io/badge/FastAPI-009688?style=flat-square&logo=fastapi&logoColor=white)
![dlib](https://img.shields.io/badge/dlib-000000?style=flat-square&logo=dlib&logoColor=white)
![OpenCV](https://img.shields.io/badge/OpenCV-5C3EE8?style=flat-square&logo=opencv&logoColor=white)
![Oracle Cloud](https://img.shields.io/badge/Oracle_Cloud-F80000?style=flat-square&logo=oracle&logoColor=white)

> 앱 설치 없이 브라우저에서 바로 즐기는 얼굴 인식 AR 이모지 필터 서비스입니다. 카페 등 오프라인 공간의 웹캠 포토존을 혁신적으로 업그레이드합니다.

## 목차

- [프로젝트 개요](#프로젝트-개요)
- [주요 기능](#주요-기능)
- [기술 스택](#기술-스택)
- [시스템 아키텍처](#시스템-아키텍처)
- [설치 및 실행](#설치-및-실행)
- [API 문서](#api-문서)
- [팀원 소개](#팀원-소개)
- [라이선스](#라이선스)

## 프로젝트 개요

FilMoji는 웹 브라우저 환경에서 얼굴 인식 기술을 활용한 AR 이모지 필터를 제공하는 서비스입니다. 별도의 애플리케이션 설치 없이 웹캠을 통해 사용자의 얼굴에 다양한 이모지 필터를 실시간으로 적용할 수 있으며, 이를 통해 오프라인 공간(예: 카페, 매장)의 포토존 경험을 혁신하는 것을 목표로 합니다.

## 주요 기능

*   **실시간 AR 이모지 필터**: 웹 브라우저를 통해 웹캠 영상에 얼굴 인식 기반의 AR 이모지 필터를 실시간으로 적용합니다.
*   **앱 설치 불필요**: 웹 기반 서비스로, 사용자는 별도의 앱 설치 없이 URL 접속만으로 서비스를 이용할 수 있습니다.
*   **포토존 활용**: 카페, 매장 등 다양한 오프라인 공간에서 고객 참여형 포토존으로 활용될 수 있습니다.
*   **이미지 저장 및 관리**: 적용된 필터 이미지를 저장하고 관리하는 기능을 제공합니다.

## 기술 스택

FilMoji 프로젝트는 다음과 같은 기술 스택으로 구성되어 있습니다.

*   **개발 언어**:
    *   Java: 백엔드 애플리케이션 개발
    *   Python: AI/ML 기반 랜드마크 서버 개발
    *   JavaScript: 프론트엔드 동적 기능 구현
    *   HTML/CSS: 웹 페이지 구조 및 스타일링
*   **백엔드**:
    *   Spring Boot: Java 기반의 RESTful API 및 웹 서비스 개발 프레임워크
    *   MySQL: 관계형 데이터베이스
    *   Spring Data JPA: 데이터베이스 ORM (Object-Relational Mapping)
    *   Spring Security & JWT: 사용자 인증 및 권한 관리, 토큰 기반 보안
*   **프론트엔드**:
    *   HTML/CSS: 웹 페이지 마크업 및 스타일링
    *   Thymeleaf: 서버 사이드 템플릿 엔진 (Spring Boot와 연동)
    *   JavaScript (Vanilla): 클라이언트 사이드 로직 및 UI 상호작용
*   **AI / ML**:
    *   FastAPI: Python 기반의 고성능 웹 프레임워크 (랜드마크 서버 구현)
    *   dlib: 얼굴 감지 및 얼굴 랜드마크 예측 라이브러리
    *   OpenCV-Python: 이미지 및 비디오 처리 라이브러리
*   **클라우드 / 스토리지**:
    *   Oracle Cloud Infrastructure (OCI) Object Storage: 이미지 파일 저장 및 관리

## 시스템 아키텍처

FilMoji는 크게 두 개의 주요 서비스와 데이터베이스, 클라우드 스토리지를 포함하는 분산 아키텍처로 구성됩니다.

1.  **Spring Boot 백엔드**:
    *   사용자 인증, 데이터 관리(회원 정보, 이미지 메타데이터 등), 웹 페이지 렌더링(Thymeleaf) 및 API 엔드포인트를 담당합니다.
    *   MySQL 데이터베이스와 연동하여 데이터를 저장하고 관리합니다.
    *   필요에 따라 FastAPI 랜드마크 서버와 통신하여 이미지 처리 요청을 보냅니다.

2.  **FastAPI 랜드마크 서버**:
    *   Python 기반의 고성능 API 서버로, dlib과 OpenCV를 활용하여 얼굴 인식 및 랜드마크 추출 기능을 제공합니다.
    *   Spring Boot 백엔드로부터 이미지 처리 요청을 받아 결과를 반환합니다.

3.  **MySQL 데이터베이스**:
    *   애플리케이션의 모든 구조화된 데이터를 저장합니다.

4.  **Oracle Cloud Object Storage**:
    *   사용자가 업로드하거나 필터링된 이미지와 같은 비정형 데이터를 저장하고 관리합니다.

두 서비스는 Docker 컨테이너로 격리되어 실행되며, Docker 네트워크를 통해 서로 통신합니다.

```
+-------------------+       +-----------------------+       +---------------------+
|   Web Browser     |       |   Spring Boot Backend |       |   FastAPI Landmark  |
| (HTML, CSS, JS)   |<----->|   (Java, Thymeleaf)   |<----->|   Server (Python)   |
+-------------------+       +-----------------------+       +---------------------+
         ^                          |                                 |
         |                          |                                 |
         |                          v                                 v
         |                  +-----------------+               +-------------------------+
         |                  |   MySQL DB      |               |   OCI Object Storage    |
         +------------------| (Data Storage)  |<--------------| (Image Storage)         |
                            +-----------------+               +-------------------------+
```

## 설치 및 실행

FilMoji 프로젝트를 로컬 환경에서 Docker 컨테이너를 사용하여 실행하는 방법을 안내합니다.

### 전제 조건

*   Java Development Kit (JDK) 17 이상
*   Python 3.11 이상
*   Docker Desktop (또는 Docker Engine) 설치
*   Git (소스 코드 클론용)
*   MySQL (로컬 설치 또는 Docker 컨테이너 사용)

### 1. 프로젝트 클론

```bash
git clone [프로젝트_저장소_URL]
cd deepLearning
```

### 2. Spring Boot JAR 파일 빌드

`backend` 디렉토리로 이동하여 Spring Boot 애플리케이션을 실행 가능한 JAR 파일로 빌드합니다.

```bash
cd backend
./gradlew bootJar
cd .. # deepLearning 루트 디렉토리로 돌아오기
```

### 3. Dockerfile 수정 및 이미지 빌드

#### 3.1. FastAPI `Dockerfile` 수정

`landmark_server/Dockerfile` 파일을 열고 `dlib` 및 `OpenCV` 빌드에 필요한 시스템 종속성을 설치하도록 다음 내용을 확인합니다.

```dockerfile
# landmark_server/Dockerfile
FROM python:3.11-slim-bullseye

RUN apt-get update && apt-get install -y \
    cmake \
    build-essential \
    libglib2.0-0 \
    libsm6 \
    libxrender1 \
    libxext6 \
    libxml2 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY requirements.txt .nRUN pip install --no-cache-dir -r requirements.txt

COPY app/ ./app/

# dlib 모델 파일 복사 (필요한 경우)
# model/shape_predictor_68_face_landmarks.dat 파일이 있다면 주석을 해제하세요.
# COPY model/shape_predictor_68_face_landmarks.dat ./model/

EXPOSE 8000

CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]
```

#### 3.2. `requirements.txt` 확인

`landmark_server/requirements.txt` 파일을 확인합니다.

```
fastapi
uvicorn
python-multipart
dlib
opencv-python
numpy
```

#### 3.3. Docker 이미지 빌드

각 애플리케이션의 루트 디렉토리에서 다음 명령어를 실행하여 Docker 이미지를 빌드합니다.

```bash
# Spring Boot 이미지 빌드
cd backend
docker build -t spring-boot-backend .
cd ..

# FastAPI 이미지 빌드
cd landmark_server
docker build -t fastapi-landmark-server .
cd ..
```

### 4. Docker 네트워크 생성

두 컨테이너가 서로 통신할 수 있도록 Docker 네트워크를 생성합니다.

```bash
docker network create filmoji-network
```

### 5. Spring Boot `application.properties` 수정 및 재빌드

Spring Boot 애플리케이션이 Docker 네트워크 내의 MySQL 및 FastAPI 컨테이너와 통신할 수 있도록 `backend/src/main/resources/application.properties` 파일을 수정합니다.

```properties
# backend/src/main/resources/application.properties

# MySQL 연결 정보
# DB_HOST 환경 변수가 없으면 filmoji-mysql을 기본값으로 사용
# DB_NAME 환경 변수가 없으면 fil-moji를 기본값으로 사용
spring.datasource.url=jdbc:mysql://${DB_HOST:filmoji-mysql}:3306/${DB_NAME:fil-moji}?serverTimezone=Asia/Seoul&useSSL=false

spring.datasource.username=root
spring.datasource.password=your_mysql_root_password # 위에서 설정한 MySQL 비밀번호

# FastAPI 서버 URL (Spring Boot에서 FastAPI로 요청을 보낼 경우)
# 예시: python.server.url=http://filmoji-landmark:8000
# 이 부분은 실제 Spring Boot 코드에서 FastAPI 서버를 호출하는 방식에 따라 달라질 수 있습니다.

# ... (나머지 설정)
```

**수정 후에는 Spring Boot JAR 파일과 Docker 이미지를 다시 빌드해야 합니다.**

```bash
# Spring Boot JAR 파일 재빌드
cd backend
./gradlew bootJar
cd ..

# Spring Boot Docker 이미지 재빌드
cd backend
docker build -t spring-boot-backend .
cd ..
```

### 6. MySQL 컨테이너 실행 (선택 사항)

로컬에 MySQL이 설치되어 있지 않거나 Docker 컨테이너로 MySQL을 사용하려면 다음 명령어를 실행합니다. `your_mysql_root_password`를 원하는 비밀번호로 변경하세요.

```bash
docker run -d --name filmoji-mysql --network filmoji-network -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=your_mysql_root_password \
  -e MYSQL_DATABASE=fil-moji mysql:8.0
```

### 7. 컨테이너 실행

이제 모든 준비가 완료되었습니다. 다음 명령어를 사용하여 각 애플리케이션 컨테이너를 실행합니다.

```bash
# Spring Boot 컨테이너 실행
docker run -d --name filmoji-backend --network filmoji-network -p 8080:8080 \
  -e DB_HOST=filmoji-mysql \
  -e DB_NAME=fil-moji \
  spring-boot-backend:latest

# FastAPI 컨테이너 실행
docker run -d --name filmoji-landmark --network filmoji-network -p 8000:8000 fastapi-landmark-server:latest
```

### 8. 실행 확인

*   실행 중인 컨테이너 확인:
    ```bash
    docker ps
    ```
*   컨테이너 로그 확인:
    ```bash
    docker logs filmoji-backend
    docker logs filmoji-landmark
    ```
# docker logs filmoji-mysql (MySQL 컨테이너 실행 시)

    모든 컨테이너가 `Up` 상태이고 로그에 오류가 없다면, 웹 브라우저에서 `http://localhost:8080`으로 접속하여 Spring Boot 애플리케이션에 접근할 수 있습니다.

## API 문서

FastAPI는 자동으로 OpenAPI(Swagger UI) 문서를 생성합니다. FastAPI 서버가 실행 중일 때 `http://localhost:8000/docs` 또는 `http://localhost:8000/redoc`에서 API 문서를 확인할 수 있습니다.

Spring Boot API 문서는 별도의 도구(예: Springdoc OpenAPI)를 사용하여 생성할 수 있습니다.

## 팀원 소개

*   [kjaewon4](https://github.com/kjaewon4)

## 라이선스

이 프로젝트는 MIT 라이선스에 따라 배포됩니다. 자세한 내용은 `LICENSE` 파일을 참조하십시오.
