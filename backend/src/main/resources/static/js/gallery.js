document.addEventListener("DOMContentLoaded", async () => {
    const res = await fetch("/api/photos/gallery", {
        method: "GET",
    });

    const photos = await res.json();
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

        const div = document.createElement("div");
        div.className = "photo-item";

        div.innerHTML = `
            <img src="${photo.parUrl}" alt="user photo" class="photo-img">
            <div class="photo-meta">
                필터:
                ${src ? `<img src="${src}" alt="emoji" class="emoji-icon">` : emoji}
                (${position})
            </div>
        `;

        const deleteBtn = document.createElement("button");
        deleteBtn.className = "delete-btn";
        deleteBtn.innerText = "삭제";
        deleteBtn.addEventListener("click", () => deletePhoto(photo.photoUrl, deleteBtn));

        div.appendChild(deleteBtn);
        grid.appendChild(div);

    });
});

async function deletePhoto(fileName, btnElement) {
    if (!confirm("정말 삭제하시겠습니까?")) return;

    const res = await fetch(`/api/photos?path=${fileName}`, {
      method: "DELETE"
    });

    if (res.ok) {
        btnElement.closest(".photo-item").remove();
    } else {
        alert("삭제 실패");
    }
}