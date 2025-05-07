from fastapi import APIRouter, UploadFile, File
from landmark_server.landmark_utils import get_landmark_coords

router = APIRouter()

@router.post("/")
async def get_landmarks(file: UploadFile = File(...)):
    image_bytes = await file.read()
    coords = get_landmark_coords(image_bytes)
    return coords