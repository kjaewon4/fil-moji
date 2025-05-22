import cv2
import dlib
import numpy as np
import os
from tempfile import NamedTemporaryFile

MODEL_PATH = "model/shape_predictor_68_face_landmarks.dat"

face_detector = dlib.get_frontal_face_detector() # dlib의 기본 얼굴 탐지기
landmark_predictor = dlib.shape_predictor(MODEL_PATH) # 68개 얼굴 랜드마크를 예측하는 사전학습 모델 불러오기

async def extract_landmarks(file):
    # FastAPI에서 받은 UploadFile을 실제 디스크 파일로 저장 (OpenCV에서 바로 읽기 위함)
    with NamedTemporaryFile(delete=False, suffix=".jpg") as tmp:
        tmp.write(await file.read()) 
        tmp_path = tmp.name

    # OpenCV로 이미지 로드 후 흑백 이미지로 변환 (dlib은 grayscale 입력만 받음)
    img = cv2.imread(tmp_path)
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    # 얼굴 탐지 수행 → 결과는 dlib.rectangle 객체의 리스트
    faces = face_detector(gray)

    result = []

    for face_id, face in enumerate(faces):
        landmarks = landmark_predictor(gray, face)
        face_landmarks = []
        for i in range(68):
            x = landmarks.part(i).x
            y = landmarks.part(i).y
            face_landmarks.append({
                "face_id": face_id,
                "index": i,
                "x": x,
                "y": y
            })
        result.append(face_landmarks)

    # 임시 저장한 이미지 삭제 후 결과 반환
    os.remove(tmp_path)
    return result