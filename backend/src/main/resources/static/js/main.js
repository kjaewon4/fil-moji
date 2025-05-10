const video = document.getElementById("video");

navigator.mediaDevices.getUserMedia({video: true})
    .then((stream) => {
        video.srcObject = stream;
    })
    .catch((err) => {
        alert("카메라 접근 실패: " + err);
    });