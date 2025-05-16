document.addEventListener("DOMContentLoaded", async () => {
    const res = await fetch("/api/photos/gallery", {
        method: "GET",
        // credentials: "include"
    });

    const photos = await res.json();
    const grid = document.getElementById("photoGrid");

    photos.forEach(photo => {

        try {
            const info = JSON.parse(photo.filterInfo);
            emoji = info.emoji || "미사용";
            position = info.position || "-";
        } catch (e) {
            console.warn("filterInfo 파싱 오류:", photo.filterInfo);
        }

        const div = document.createElement("div");
        div.className = "photo-item";

        div.innerHTML = `
            <img src="${photo.parUrl}" alt="user photo">
            <p>필터: ${emoji} (${position})</p>
        `;

        grid.appendChild(div);
    });
});
