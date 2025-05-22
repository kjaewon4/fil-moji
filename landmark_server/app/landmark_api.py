from fastapi import APIRouter, UploadFile, File
from fastapi.responses import JSONResponse
from app.landmark_utils import extract_landmarks

router = APIRouter()

@router.post("/landmark")
async def landmarks(file: UploadFile = File(...)):
    try:
        result = await extract_landmarks(file)
        return JSONResponse(content={"faces": result})
    except Exception as e:
        import traceback
        traceback.print_exc()  # 전체 에러 로그 출력
        return JSONResponse(status_code=500, content={"error": str(e)})    