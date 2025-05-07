import cv2
import numpy as np
import mediapipe as mp

mp_face_mesh = mp.solutions.face_mesh

LANDMARK_IDX = {
    "left_eye": 33,
    "right_eye": 263,
    "nose": 1,
    "mouth": 13
}

def get_landmark_coords(image_bytes: bytes):
    np_img = np.frombuffer(image_bytes, np.uint8)
    img = cv2.imdecode(np_img, cv2.IMREAD_COLOR)

    with mp_face_mesh.FaceMesh(static_image_mode=True) as face_mesh:
        results = face_mesh.process(cv2.cvtColor(img, cv2.COLOR_BGR2RGB))
        if not results.multi_face_landmarks:
            return {"error": "No face detected"}

        face = results.multi_face_landmarks[0]
        h, w, _ = img.shape

        coords = {
            name: [int(face.landmark[idx].x * w), int(face.landmark[idx].y * h)]
            for name, idx in LANDMARK_IDX.items()
        }
        return coords