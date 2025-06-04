document.addEventListener("DOMContentLoaded", async () => {
    // 1) 서버에서 사진 목록을 가져옵니다.
    const res = await fetch("/api/photos/gallery", {
        method: "GET",
    });
    const photos = await res.json();

    // 2) HTML 상에서 <section id="photoGrid" class="photo-grid"> 요소를 가져옵니다.
    const grid = document.getElementById("photoGrid");

    photos.forEach(photo => {
        let emoji = "미사용";
        let position = "-";
        let src = null;

        try {
            const info = JSON.parse(photo.filterInfo);
            emoji = info.emoji || "미사용";
            position = info.position || "-";
            src = info.src || null;
        } catch (e) {
            console.warn("filterInfo 파싱 오류:", photo.filterInfo);
        }

        // 3) 카드 최상위 div를 생성하고 클래스 지정
        const card = document.createElement("div");
        card.className = "photo-card";

        // 4) innerHTML 로 카드 내부 구조를 만듭니다.
        card.innerHTML = `
      <!-- 카드 헤더: 타이틀 + info 아이콘 -->
      <div class="card-header">
        <span class="card-title">AR Filter</span>
        <button class="info-btn" title="정보">
          &#9432;
        </button>
      </div>

      <!-- 카드 본문: 사진 -->
      <div class="card-body">
        <img src="${photo.parUrl}" alt="user photo" class="card-img" />
      </div>

      <!-- 카드 푸터: 소셜 아이콘 + 이모지 영역 -->
      <div class="card-footer">
        <div class="social-icons">
          <!-- 실제 아이콘 경로로 교체하세요 -->
              <button class="social-icon download-btn" title="다운로드"></button>

<!--          <img src="/styles/kakao-talk.png" -->
<!--               alt="kakaotalk" class="social-icon" />-->
<!--  -->
        </div>
        <div class="emoji-used">
          ${ src
            ? `<img src="${src}" alt="emoji" class="emoji-icon" />`
            : `<span class="emoji-text">${emoji}</span>`
        }
          <span class="emoji-label">사용된 이모지</span>
        </div>
      </div>

      <!-- 삭제 버튼 -->
      <button class="delete-btn">삭제</button>
    `;

        // 5) 삭제 버튼 클릭 시, 최상위 .photo-card 요소를 찾아서 삭제하도록 수정
        const deleteBtn = card.querySelector(".delete-btn");
        deleteBtn.addEventListener("click", () => {
            deletePhoto(photo.photoUrl, deleteBtn);
        });
        // 다운로드 버튼 클릭 이벤트 연결
        const downloadBtn = card.querySelector(".download-btn");
        downloadBtn.addEventListener("click", () => {
            // 예시: 파일명으로 photo.parUrl에서 마지막 경로만 잘라서 사용
            const urlSegments = photo.parUrl.split("/");
            const filename = urlSegments[urlSegments.length - 1];
            downloadImage(photo.parUrl, filename);
        });

        // 6) 그리드 컨테이너에 카드(div.photo-card)를 추가
        grid.appendChild(card);
    });
});

// 7) deletePhoto 함수 내부에서 삭제 대상 요소 선택자를 .photo-card 로 수정
async function deletePhoto(fileName, btnElement) {
    if (!confirm("정말 삭제하시겠습니까?")) return;

    const res = await fetch(`/api/photos?path=${fileName}`, {
        method: "DELETE"
    });

    if (res.ok) {
        // 기존: btnElement.closest(".photo-item").remove();
        // 수정: 카드 최상위 요소인 .photo-card 을 찾아서 제거
        btnElement.closest(".photo-card").remove();
    } else {
        alert("삭제 실패");
    }
}

// ②: 이미지 URL을 fetch해서 Blob으로 받아오고, 다운로드 트리거
async function downloadImage(imageUrl, filename) {
    try {
        const response = await fetch(imageUrl, { mode: 'cors' });
        // (외부 도메인이라면 CORS가 허용돼 있어야 함)

        if (!response.ok) {
            throw new Error("네트워크 응답에 문제가 있습니다.");
        }

        const blob = await response.blob();
        const blobUrl = URL.createObjectURL(blob);

        // 임시로 a 태그 생성하여 클릭 트리거
        const a = document.createElement("a");
        a.href = blobUrl;
        a.download = filename; // 예: "abc123.jpg"
        document.body.appendChild(a);
        a.click();

        // 메모리 해제
        setTimeout(() => {
            URL.revokeObjectURL(blobUrl);
            document.body.removeChild(a);
        }, 100);

    } catch (error) {
        console.error("이미지 다운로드 오류:", error);
        alert("이미지를 다운로드할 수 없습니다.");
    }
}