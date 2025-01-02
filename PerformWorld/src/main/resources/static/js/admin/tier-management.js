document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("addTierModal");
    const form = document.getElementById("addTierForm");
    const addTierButton = document.querySelector(".addTierBtn");  // 클래스 이름을 사용하여 선택

    function toggleModal(show) {
        modal.style.display = show ? "block" : "none";
    }

    document.querySelector(".add-tier-btn").addEventListener("click", () => toggleModal(true));
    document.querySelector(".close").addEventListener("click", () => toggleModal(false));

    window.addEventListener("click", (e) => {
        if (e.target === modal) toggleModal(false);
    });

    addTierButton.addEventListener("click", async (e) => {
        e.preventDefault();

        const tierName = document.getElementById("tierName").value.trim();
        const minSpent = parseFloat(document.getElementById("minSpent").value.trim());
        const maxSpent = parseFloat(document.getElementById("maxSpent").value.trim());
        const discountRate = parseFloat(document.getElementById("discountRate").value.trim());

        if (!tierName || isNaN(minSpent) || isNaN(maxSpent) || isNaN(discountRate) || minSpent >= maxSpent || discountRate < 0 || discountRate > 100) {
            alert("유효한 값을 입력하세요.");
            return;
        }

        try {
            addTierButton.disabled = true;
            const response = await axios.post("/admin/addTier", { tierName, minSpent, maxSpent, discountRate });
            alert("새 Tier가 추가되었습니다!");
            form.reset();
            toggleModal(false);

            const tbody = document.querySelector("#tierTable tbody");
            const newRow = document.createElement("tr");
            newRow.innerHTML = `
                <td>${response.data.tierId}</td>
                <td>${response.data.tierName}</td>
                <td>${response.data.minSpent} 원</td>
                <td>${response.data.maxSpent} 원</td>
                <td>${response.data.discountRate}%</td>
            `;
            tbody.appendChild(newRow);
        } catch (error) {
            const errorMessage = error.response?.data?.message || "알 수 없는 오류가 발생했습니다.";
            alert(`오류: ${errorMessage}`);
        } finally {
            addTierButton.disabled = false;
        }
    });
});