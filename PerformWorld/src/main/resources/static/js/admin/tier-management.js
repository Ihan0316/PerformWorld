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

    // 새 Tier 추가 버튼 클릭 시
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
        } catch (error) {
            const errorMessage = error.response?.data?.message || "알 수 없는 오류가 발생했습니다.";
            alert(`오류: ${errorMessage}`);
        } finally {
            addTierButton.disabled = false;
        }
    });

    // Edit Tier Modal 관련
    const editModal = document.getElementById("editTierModal");
    const editForm = document.getElementById("editTierForm");

    let selectedTierId = null; // 현재 수정 중인 Tier ID

    // 모달 보이기/숨기기 함수 (Edit Tier Modal)
    function toggleEditModal(show) {
        editModal.style.display = show ? "block" : "none";
    }

    // 수정 버튼 클릭 시
    window.updateTier = function(button) {
        selectedTierId = button.getAttribute("data-tier-id");
        console.log("Selected Tier ID (updateTier function):", selectedTierId); // 확인 로그

        try {
            axios.get(`/admin/tier/${selectedTierId}`)
                .then((response) => {
                    const tier = response.data;
                    console.log("API Response:", tier); // 수정할 데이터 확인 로그

                    // 수정 모달에 데이터 채우기
                    document.getElementById("editTierName").value = tier.tierName;
                    document.getElementById("editMinSpent").value = tier.minSpent;
                    document.getElementById("editMaxSpent").value = tier.maxSpent;
                    document.getElementById("editDiscountRate").value = tier.discountRate;

                    // 수정 모달 표시
                    toggleEditModal(true);
                })
                .catch((error) => {
                    const errorMessage = error.response?.data?.message || "Tier 정보를 불러오는 데 실패했습니다.";
                    alert(`오류: ${errorMessage}`);
                });
        } catch (error) {
            console.error("Failed to fetch tier data:", error);
        }
    };

// 수정 폼 제출 시
    editForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const tierName = document.getElementById("editTierName").value.trim();
        const minSpent = parseFloat(document.getElementById("editMinSpent").value.trim());
        const maxSpent = parseFloat(document.getElementById("editMaxSpent").value.trim());
        const discountRate = parseFloat(document.getElementById("editDiscountRate").value.trim());

        // 유효성 검사
        if (!tierName ||
            isNaN(minSpent) || isNaN(maxSpent) || isNaN(discountRate) ||
            minSpent < 0 || maxSpent < 0 || discountRate < 0 ||
            minSpent >= maxSpent || discountRate > 100) {
            alert("유효한 값을 입력하세요.");
            return;
        }

        try {
            await axios.put(`/admin/tier/${selectedTierId}`, { tierName, minSpent, maxSpent, discountRate });
            alert("Tier가 성공적으로 수정되었습니다!");
            console.log("selectedTierId (after update):", selectedTierId);

            // 수정된 데이터로 테이블 갱신
            const row = document.querySelector(`tr[data-tier-id="${selectedTierId}"]`);
            console.log("Row element:", row); // 수정된 row 확인 로그

            if (row) {
                row.cells[1].innerText = tierName;
                row.cells[2].innerText = minSpent + " 원";
                row.cells[3].innerText = maxSpent + " 원";
                row.cells[4].innerText = discountRate + "%";
            }
            // 수정 모달 닫기
            toggleEditModal(false);

            // 페이지 새로고침
            location.reload(); // 페이지 새로고침
        } catch (error) {
            console.error("Error details:", error);
            const errorMessage = error.response?.data?.message || error.message || "알 수 없는 오류가 발생했습니다.";
            alert(`오류: ${errorMessage}`);
        }
    });

    // 수정 모달 닫기 이벤트
    document.querySelector("#editTierModal .close").addEventListener("click", () => toggleEditModal(false));

    // 수정 모달 외부 클릭 시 닫기
    window.addEventListener("click", (e) => {
        if (e.target === editModal) toggleEditModal(false);
    });
});