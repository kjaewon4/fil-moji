from fastapi import FastAPI
from .landmark_api import router
from .landmark_utils import get_landmark_coords

app = FastAPI(title="MediaPipe Landmark Server")
app.include_router(router, prefix="/api/landmark", tags=["Landmark"])

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("landmark_server.main:app", host="0.0.0.0", port=8001, reload=False)

