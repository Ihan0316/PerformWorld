document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("addTierModal");
    const form = document.getElementById("addTierForm");
    const addTierButton = document.querySelector(".addTierBtn");

    // 숫자 입력에 대한 소숫점 방지 및 0 이상으로 제한
    document.querySelectorAll("#maxSpent, #minSpent, #discountRate").forEach(input => {
        input.addEventListener("input", function (e) {
            let value = e.target.value;

            // 소숫점 입력 방지
            value = value.replace(/\./g, '');

            // 값이 0보다 작은 경우 0으로 설정
            if (parseInt(value) < 0) {
                value = '0';  // 'value.replace = '0';' 오류를 수정
            }

            // 수정된 값을 다시 입력 필드에 설정
            e.target.value = value;
        });
    });

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

        // 유효성 검사
        if (!tierName ||
            isNaN(minSpent) || isNaN(maxSpent) || isNaN(discountRate) ||
            minSpent < 0 || maxSpent < 0 || discountRate < 0 ||
            minSpent >= maxSpent || discountRate > 100) {
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