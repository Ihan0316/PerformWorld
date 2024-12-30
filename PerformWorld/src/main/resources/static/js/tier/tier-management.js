$(document).ready(function() {
    // 모달 열기
    function openAddTierModal() {
        $('#addTierModal').fadeIn();  // 애니메이션 추가 (모달 열기)
    }

    // 모달 닫기
    function closeAddTierModal() {
        $('#addTierModal').fadeOut();  // 애니메이션 추가 (모달 닫기)
    }

    // Add User Tier 버튼 클릭 시, 모달 열기
    $(".add-tier-btn").on("click", openAddTierModal);

    // 모달 외부 클릭 시 모달 닫기
    $(window).on("click", function(event) {
        var modal = document.getElementById('addTierModal');
        if (event.target == modal) {
            closeAddTierModal();
        }
    });

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
});