document.addEventListener("DOMContentLoaded", function () {
    const addModal = document.getElementById("addTierModal");
    const addForm = document.getElementById("addTierForm");
    const addTierButton = document.getElementById("addTierBtn");
    const openModalButton = document.getElementById("openAddTierModalBtn");
    const cancelButton = document.querySelector("#addTierModal .btn-secondary");

    function toggleAddModal(show) {
        if (show) {
            addModal.classList.add("show");
            document.getElementById("tierName").focus();
        } else {
            addModal.classList.remove("show");
        }
    }

    openModalButton.addEventListener("click", () => toggleAddModal(true));
    document.querySelector("#addTierModal .btn-close").addEventListener("click", () => toggleAddModal(false));
    cancelButton.addEventListener("click", () => toggleAddModal(false));

    window.addEventListener("click", (e) => {
        if (e.target === addModal) toggleAddModal(false);
    });

    async function getExistingTiers() {
        try {
            const response = await axios.get("/admin/getTiers");
            return response.data.map(tier => {
                return { tierName: tier.tierName, minSpent: tier.minSpent, maxSpent: tier.maxSpent };
            });
        } catch (error) {
            console.error("Failed to fetch existing tiers:", error);
            return [];
        }
    }

    function validateInputs(tierName, minSpent, maxSpent, discountRate, maxExistingSpent) {
        if (!tierName || isNaN(minSpent) || isNaN(maxSpent) || isNaN(discountRate) ||
            minSpent < 0 || maxSpent < 0 || discountRate < 0 ||
            minSpent >= maxSpent || discountRate > 100 ||
            minSpent <= maxExistingSpent) { // 추가된 유효성 검사
            return "유효한 값을 입력하세요.";
        }
        return null;
    }

    async function addTier(tierName, minSpent, maxSpent, discountRate) {
        try {
            addTierButton.disabled = true;
            const response = await axios.post("/admin/addTier", { tierName, minSpent, maxSpent, discountRate });
            alert("새 Tier가 추가되었습니다!");
            addForm.reset();
            toggleAddModal(false);
            return response.data;
        } catch (error) {
            const errorMessage = error.response?.data?.message || "알 수 없는 오류가 발생했습니다.";
            alert(`오류: ${errorMessage}`);
            return null;
        } finally {
            addTierButton.disabled = false;
        }
    }

    addTierButton.addEventListener("click", async (e) => {
        e.preventDefault();

        const tierName = document.getElementById("tierName").value.trim();
        const minSpent = parseFloat(document.getElementById("minSpent").value.trim());
        const maxSpent = parseFloat(document.getElementById("maxSpent").value.trim());
        const discountRate = parseFloat(document.getElementById("discountRate").value.trim());

        const existingTiers = await getExistingTiers();

        // 중복 이름 체크
        const isDuplicateName = existingTiers.some(tier => tier.tierName === tierName);
        if (isDuplicateName) {
            alert("중복되는 Tier 이름이 있습니다. 다른 이름을 입력해주세요.");
            return;
        }

        // 중복 범위 체크
        const isDuplicateRange = existingTiers.some(tier => {
            return (minSpent >= tier.minSpent && minSpent <= tier.maxSpent) ||
                (maxSpent >= tier.minSpent && maxSpent <= tier.maxSpent) ||
                (minSpent <= tier.minSpent && maxSpent >= tier.maxSpent);
        });

        if (isDuplicateRange) {
            alert("중복되는 Tier 범위가 있습니다. 다른 값을 입력해주세요.");
            return;
        }

        // 가장 큰 maxSpent 찾기
        const maxExistingSpent = Math.max(...existingTiers.map(tier => tier.maxSpent), -Infinity);

        // 입력값 유효성 검사
        const validationError = validateInputs(tierName, minSpent, maxSpent, discountRate, maxExistingSpent);
        if (validationError) {
            alert(validationError);
            return;
        }

        // 등급 추가
        const newTierData = await addTier(tierName, minSpent, maxSpent, discountRate);
        if (newTierData) {
            location.reload();
        }
    });

    document.querySelectorAll("#maxSpent, #minSpent, #discountRate").forEach(input => {
        input.addEventListener("input", function (e) {
            let value = e.target.value.replace(/\./g, '');
            if (parseInt(value) < 0) {
                value = '0';
            }
            e.target.value = value;
        });
    });
});
