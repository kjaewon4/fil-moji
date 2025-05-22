// 선택된 필터 관련 변수
let selectedFilter = null;
let selectedFilterName = null;

// 이모지 필터 선택 함수
function selectedFilterFunc(name) {
    selectedFilter = FILTER_MAP[name];
    selectedFilterName = name;
    console.log("선택된 필터:", name);
}

// 카메라 연결
const video = document.getElementById("video");
connectCamera(video);

/**
 * 웹캠 연결 함수
 */
function connectCamera(videoElement) {
    navigator.mediaDevices.getUserMedia({ video: true })
        .then((stream) => {
            videoElement.srcObject = stream;
        })
        .catch((err) => {
            alert("카메라 접근 실패: " + err);
        });
}

/**
 * 촬영 후 Spring 서버로 전송
 */
async function sendToSpringServer() {
    const isUserLoggedIn = await isLoggedIn();
    if (!isUserLoggedIn) return showLoginModal();

    const canvas = captureVideoToCanvas(video);
    const blob = await canvasToBlob(canvas);

    const filterInfo = {
        emoji: selectedFilterName || "none",
        position: selectedFilter
            ? landmarkIndexToPosition(selectedFilter.landmarkIndex)
            : "unknown",
        src: selectedFilter.src || "none"
    };

    const formData = new FormData();
    formData.append("file", blob, "capture.jpg");
    formData.append("filterInfo", JSON.stringify(filterInfo));

    const res = await fetch("http://localhost:8080/api/photos/upload", {
        method: "POST",
        body: formData
    });

    const result = await res.json();
    console.log("📸 Spring 응답:", result);
}

/**
 * 로그인 상태 확인
 */
async function isLoggedIn() {
    const res = await fetch("http://localhost:8080/api/auth/check", {
        method: "GET",
        credentials: "include"
    });
    return res.ok;
}

/**
 * 로그인 모달 표시
 */
function showLoginModal() {
    const modal = document.getElementById("loginModal");
    modal.style.display = "block";
}

/**
 * 로그인 모달 닫기
 */
function closeModal() {
    const modal = document.getElementById("loginModal");
    modal.style.display = "none";
}

/**
 * 비디오를 캔버스로 캡처
 */
function captureVideoToCanvas(videoElement) {
    const canvas = document.createElement("canvas");
    canvas.width = videoElement.videoWidth;
    canvas.height = videoElement.videoHeight;

    const ctx = canvas.getContext("2d");
    ctx.drawImage(videoElement, 0, 0, canvas.width, canvas.height);
    return canvas;
}

/**
 * 캔버스를 Blob으로 변환
 */
function canvasToBlob(canvas) {
    return new Promise((resolve) => {
        canvas.toBlob((blob) => resolve(blob), "image/jpeg");
    });
}

/**
 * 랜드마크 인덱스를 위치 이름으로 매핑
 */
function landmarkIndexToPosition(index) {
    if (Array.isArray(index)) {
        const [i1, i2] = index;
        // 눈 위치
        if ((i1 === 33 && i2 === 263) || (i1 === 362 && i2 === 133)) {
            return "eyes";
        }
    }

    switch (index) {
        case 1:
        case 6:
        case 9:
        case 10:
            return "forehead";
        case 168:
            return "topHead";
        case 197:
        case 5:
            return "nose";
        case 152:
            return "chin";
        default:
            return "face";
    }
}

/**
 * FastAPI 요청
 */
async function getLandmarksFromServer(blob) {
    const formData = new FormData();
    formData.append("file", blob, "frame.jpg");

    const res = await fetch("http://localhost:8000/api/landmark", {
        method: "POST",
        body: formData,
    });

    const json = await res.json();
    console.log("서버 응답:", json);

    if (json.landmarks && Array.isArray(json.landmarks)) {
        return json.landmarks;
    } else {
        console.warn("랜드마크 없음 또는 오류 응답:", json);
        return []; // or null
    }
}

/**
 * 캔버스에 좌표 렌더링 (필터)
 */
function drawLandmarksOnCanvas(landmarks) {
    const canvas = document.getElementById("canvas");
    const ctx = canvas.getContext("2d");

    // 캔버스 사이즈를 비디오에 맞게 재설정
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;

    ctx.clearRect(0,0, canvas.width, canvas.height);  // 매 프레임 초기화


    landmarks.forEach(p => {
        ctx.beginPath(); // 새로운 경로 시작
        ctx.arc(p.x, p.y, 2, 0, 2 * Math.PI);  // x, y 좌표에 반지름 2짜리 원을 그림
        ctx.fillStyle = "red";
        ctx.fill();
    })
}

/**
 * 실시간 루프
 */
async function processLoop() {
    const video = document.getElementById("video");
    const canvas = captureVideoToCanvas(video);

    canvas.toBlob(async (blob) => {
        try {
            const landmarks = await getLandmarksFromServer(blob);
            drawLandmarksOnCanvas(landmarks);
            drawFilterOnCanvas(landmarks);
        } catch (e) {
            console.log("얼굴 없음:", e.messages);
        }
        requestAnimationFrame(processLoop); // 다음 루프 예약
    }, "image/jpeg");
}

// 최초 실행
window.addEventListener("DOMContentLoaded", () => {
    const video = document.getElementById("video");
    navigator.mediaDevices.getUserMedia({ video: true })
        .then((stream) => {
            video.srcObject = stream;
            requestAnimationFrame(processLoop); // 루프 시작
        })
        .catch((err) => {
            alert("카메라 접근 실패: " + err.message);
        });
});

/**
 * 필터 그리기
 */
function drawFilterOnCanvas(landmarks) {
    const canvas = document.getElementById("canvas");
    const ctx = canvas.getContext("2d");
    // ctx.clearRect(0,0,canvas.width, canvas.height);

    if (!selectedFilter || !selectedFilter.src || !selectedFilter.landmarkIndex) return;

    const img = new Image();
    img.src = selectedFilter.src;

    img.onload = () => {
        // 필터 위치 계산
        let x = 0, y = 0;
        if (Array.isArray(selectedFilter.landmarkIndex)) {
            const [i1, i2] = selectedFilter.landmarkIndex;
            const p1 = landmarks.find(p => p.index === i1);
            const p2 = landmarks.find(p => p.index === i2);
            if (!p1 || !p2) return;
            x = (p1.x + p2.x) / 2;
            y = (p1.y + p2.y) / 2;
        } else {
            const point = landmarks.find(p => p.index === selectedFilter.landmarkIndex);
            if (!point) return;
            x = point.x;
            y = point.y;
        }

        // 필터 이미지 그리기
        const size = 40; // 필터 크기 조절 (필요 시)
        ctx.drawImage(img, x - size / 2, y - size / 2, size, size);

    }
}