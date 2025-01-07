document.addEventListener("DOMContentLoaded", function () {
    // Add Tier Modal 관련
    const addModal = document.getElementById("addTierModal");
    const addForm = document.getElementById("addTierForm");
    const addTierButton = document.querySelector(".addTierBtn");

    // 숫자 입력에 대한 소숫점 방지 및 0 이상으로 제한
    document.querySelectorAll("#maxSpent, #minSpent, #discountRate").forEach(input => {
        input.addEventListener("input", function (e) {
            let value = e.target.value;

            // 소숫점 입력 방지
            value = value.replace(/\./g, '');

            // 값이 0보다 작은 경우 0으로 설정
            if (parseInt(value) < 0) {
                value = '0';
            }

            // 수정된 값을 다시 입력 필드에 설정
            e.target.value = value;
        });
    });

    // 모달 보이기/숨기기 함수 (Add Tier Modal)
    function toggleAddModal(show) {
        addModal.style.display = show ? "block" : "none";
    }

    document.querySelector(".add-tier-btn").addEventListener("click", () => toggleAddModal(true));
    document.querySelector("#addTierModal .close").addEventListener("click", () => toggleAddModal(false));

    window.addEventListener("click", (e) => {
        if (e.target === addModal) toggleAddModal(false);
    });

    // 기존 Tier 목록을 DB에서 가져오기 (중복 검사)
    async function getExistingTiers() {
        try {
            const response = await axios.get("/admin/getTiers");  // DB에서 기존 Tier 목록을 가져옵니다.
            return response.data.map(tier => {
                return { minSpent: tier.minSpent, maxSpent: tier.maxSpent, minSpent: tier.maxSpent, maxSpent: tier.minSpent };
            });
        } catch (error) {
            console.error("Failed to fetch existing tiers:", error);
            return [];  // DB에서 데이터를 가져오는 데 실패한 경우 빈 배열 반환
        }
    }

    // 새 Tier 추가 버튼 클릭 시
    addTierButton.addEventListener("click", async (e) => {
        e.preventDefault();

        const tierName = document.getElementById("tierName").value.trim();
        const minSpent = parseFloat(document.getElementById("minSpent").value.trim());
        const maxSpent = parseFloat(document.getElementById("maxSpent").value.trim());
        const discountRate = parseFloat(document.getElementById("discountRate").value.trim());

        const existingTiers = await getExistingTiers();  // DB에서 기존 Tier 목록을 가져옴

        // 기존 값과 중복 검사
        const isDuplicate = existingTiers.some(tier => {
            // minSpent ~ maxSpent 범위가 겹치는지 확인
            return (minSpent >= tier.minSpent && minSpent <= tier.maxSpent) ||  // 새 Tier의 minSpent가 기존 범위 내에 있는지
                (maxSpent >= tier.minSpent && maxSpent <= tier.maxSpent) ||  // 새 Tier의 maxSpent가 기존 범위 내에 있는지
                (minSpent <= tier.minSpent && maxSpent >= tier.maxSpent);   // 새 Tier의 범위가 기존 범위를 완전히 포함하는지
        });

        if (isDuplicate) {
            alert("중복되는 Tier 범위가 있습니다. 다른 값을 입력해주세요.");
            return;
        }

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
            addForm.reset();
            toggleAddModal(false);

            // 테이블에 새 Row 추가
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
            location.reload(); // 페이지 새로고침
        } catch (error) {
            const errorMessage = error.response?.data?.message || "알 수 없는 오류가 발생했습니다.";
            alert(`오류: ${errorMessage}`);
        } finally {
            addTierButton.disabled = false;
        }
    });
});