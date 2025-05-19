from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.landmark_api import router as landmark_router

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_method=["*"],
    allow_headers=["*"],
)

app.include_router(landmark_router, prefix="/api")

# 실행 명령어 
#  uvicorn app.main:app --reload
