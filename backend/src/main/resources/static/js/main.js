const video = document.getElementById("video");

navigator.mediaDevices.getUserMedia({video: true})
    .then((stream) => {
        video.srcObject = stream;
    })
    .catch((err) => {
        alert("카메라 접근 실패: " + err);
    });

async function sendToSpringServer() {

    if (!isLoggedIn()) {
        showLoginModal();
        return;
    }

    const video = document.getElementById("video");
    const canvas = document.createElement("canvas");
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;

    const ctx = canvas.getContext("2d");
    ctx.drawImage(video, 0, 0, canvas.width, canvas.height);

    canvas.toBlob(async (blob) => {
        const formData = new FormData();
        formData.append("file", blob, "capture.jpg");
        formData.append("filterInfo", JSON.stringify({
            emoji: "🎯",
            position: "nose"
        }));

        const res = await fetch("http://localhost:8080/api/photos/upload", {
            method: "POST",
            body: formData
        });

        const result = await res.json();
        console.log("📸 Spring 응답:", result);

    }, "image/jpeg");
}

async function isLoggedIn() {
    const res = await fetch("http://localhost:8080/api/auth/check", {
        method: "GET",
        credentials: "include"
    });
    return res.ok;
}

function showLoginModal() {
    const modal = document.getElementById("loginModal");
    modal.style.display = "block";
}

function closeModal() {
    const modal = document.getElementById("loginModal");
    modal.style.display = "none";
}
