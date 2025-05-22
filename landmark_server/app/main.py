from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.landmark_api import router as landmark_router

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(landmark_router, prefix="/api")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("app.main:app", host="0.0.0.0", port=8000, reload=True)
# 실행 명령어 
#  uvicorn app.main:app --reload --host 0.0.0.0 --port 8000

