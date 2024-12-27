// resources/static/js/tier-management.js

$(document).ready(function() {
    // 모달 열기
    function openAddTierModal() {
        $('#addTierModal').fadeIn();  // 애니메이션 추가 (모달 열기)
    }

    // 모달 닫기
    function closeAddTierModal() {
        $('#addTierModal').fadeOut();  // 애니메이션 추가 (모달 닫기)
    }

    // 폼 제출 시 처리
    $("#addTierForm").on("submit", function(e) {
        e.preventDefault();

        var tierData = {
            tierName: $("#tierName").val(),
            minSpent: $("#minSpent").val(),
            maxSpent: $("#maxSpent").val(),
            discountRate: $("#discountRate").val()
        };

        // 서버에 데이터를 전송하는 AJAX 요청
        $.ajax({
            url: '/admin/addTier',  // Tier 등록 처리 URL
            method: 'POST',
            data: tierData,
            success: function(response) {
                alert("New Tier Added Successfully!");

                // 폼 초기화
                $("#addTierForm")[0].reset();

                // 모달 닫기
                closeAddTierModal();

                // 새 Tier를 목록에 추가
                $(".tier-list").append(`
                    <li class="tier-item animate-box" data-animate-effect="fadeInLeft">
                        <h3>${response.tierName}</h3>
                        <p>
                            <span>Min Spent: ${response.minSpent}</span><br/>
                            <span>Max Spent: ${response.maxSpent}</span><br/>
                            <span>Discount Rate: ${response.discountRate}%</span>
                        </p>
                    </li>
                `);
            },
            error: function() {
                alert("Error adding new tier.");
            }
        });
    });

    // User Tiers 제목 클릭 시, 리스트 표시/숨기기
    const tierHeader = document.querySelector('.tier-header h2'); // 'User Tiers' 제목을 클릭하는 이벤트
    const tierList = document.querySelector('.tier-list'); // Tier 목록을 선택

    tierHeader.addEventListener('click', function() {
        // tier-list가 보이면 숨기고, 숨겨지면 보이게 한다.
        if ($(tierList).is(":visible")) {
            $(tierList).fadeOut();  // 리스트를 숨긴다.
        } else {
            $(tierList).fadeIn();  // 리스트를 보이게 한다.
        }
    });

    // Add User Tier 버튼 클릭 시, 모달 열기
    document.querySelector(".add-tier-btn").addEventListener("click", openAddTierModal);

    // 모달 외부 클릭 시 모달 닫기
    window.onclick = function(event) {
        var modal = document.getElementById('addTierModal');
        if (event.target == modal) {
            closeAddTierModal();
        }
    };
});
