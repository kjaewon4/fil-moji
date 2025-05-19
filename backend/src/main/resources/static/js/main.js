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
            : "unknown"
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
