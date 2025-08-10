# Python 3.11 기반 이미지 사용
FROM python:3.11-slim-bullseye

# CMake 및 dlib 빌드에 필요한 종속성 설치
RUN apt-get update && apt-get install -y --no-install-recommends \
     cmake \
     build-essential \
     libglib2.0-0 \
     libsm6 \
     libxrender1 \
     libxext6 \
     libxml2 \
     && rm -rf /var/lib/apt/lists/*

# 작업 디렉토리 설정
WORKDIR /app

# requirements.txt 복사 및 의존성 설치
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 애플리케이션 코드 복사
COPY app/ ./app/

# dlib 모델 파일 복사 (필요한 경우)
COPY model/shape_predictor_68_face_landmarks.dat ./model/

# FastAPI 애플리케이션이 사용할 포트 지정
EXPOSE 8000

# FastAPI 애플리케이션 실행 명령어
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]