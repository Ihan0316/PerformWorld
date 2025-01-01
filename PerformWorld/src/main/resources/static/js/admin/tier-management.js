document.addEventListener("DOMContentLoaded", function() {
    // 모달 열기
    function openAddTierModal() {
        document.getElementById('addTierModal').style.display = 'block';  // 모달 열기
    }

    // 모달 닫기
    function closeAddTierModal() {
        document.getElementById('addTierModal').style.display = 'none';  // 모달 닫기
    }

    // Add Tier 버튼 클릭 시, 모달 열기
    document.querySelector(".add-tier-btn").addEventListener("click", openAddTierModal);

    // 모달 외부 클릭 시 모달 닫기
    document.getElementById('addTierModal').addEventListener('click', function(event) {
        if (event.target === document.getElementById('addTierModal')) {
            closeAddTierModal();
        }
    });

    // 폼 제출 시 처리
    document.getElementById("addTierForm").addEventListener("submit", function(e) {
        e.preventDefault();

        var tierData = {
            tierName: document.getElementById("tierName").value,
            minSpent: document.getElementById("minSpent").value,
            maxSpent: document.getElementById("maxSpent").value,
            discountRate: document.getElementById("discountRate").value
        };

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/admin/addTier", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.onload = function() {
            if (xhr.status === 200) {
                var response = JSON.parse(xhr.responseText);
                alert("새 Tier가 추가되었습니다!");

                // 폼 초기화
                document.getElementById("addTierForm").reset();

                // 모달 닫기
                closeAddTierModal();

                // 새 Tier를 목록에 추가
                var tbody = document.querySelector("table tbody");
                var newRow = document.createElement("tr");

                newRow.innerHTML = `
                    <td>${response.tierId}</td>
                    <td>${response.tierName}</td>
                    <td>${response.minSpent} 원</td>
                    <td>${response.maxSpent} 원</td>
                    <td>${response.discountRate}%</td>
                `;
                tbody.appendChild(newRow);
            } else {
                alert("새 Tier 추가에 실패했습니다.");
            }
        };
        xhr.send(`tierName=${tierData.tierName}&minSpent=${tierData.minSpent}&maxSpent=${tierData.maxSpent}&discountRate=${tierData.discountRate}`);
    });
});