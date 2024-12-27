$(document).ready(function () {
    // 로그인 폼 제출 처리
    $("#loginForm").submit(function (e) {
        e.preventDefault(); // 기본 폼 제출을 막음

        // 입력값 가져오기
        var userId = $("input[name='userId']").val();
        var password = $("input[name='password']").val();

        // 유효성 검사
        if (!validateForm(userId, password)) {
            return;
        }

        // 로딩 애니메이션 활성화
        $("#loginButton").prop("disabled", true).val("로그인 중...");
        $("#loadingSpinner").show(); // 로딩 스피너 표시

        // AJAX로 로그인 요청
        $.ajax({
            url: '/user/login',
            type: 'POST',
            data: {
                userId: userId,
                password: password
            },
            success: function (response) {
                alert("로그인 성공!");
                window.location.href = "/user/mypage"; // 마이페이지로 리다이렉트
            },
            error: function (xhr, status, error) {
                // 로그인 실패 시 메시지 출력
                alert(xhr.responseText || "로그인 실패");
            },
            complete: function () {
                // 요청 완료 후 버튼 활성화 및 로딩 스피너 숨기기
                $("#loginButton").prop("disabled", false).val("Login");
                $("#loadingSpinner").hide();
            }
        });
    });

    // 유효성 검사 함수
    function validateForm(userId, password) {
        if (!userId || !password) {
            alert("아이디와 비밀번호를 모두 입력해주세요.");
            return false;
        }

        // 비밀번호 최소 길이 체크
        if (password.length < 6) {
            alert("비밀번호는 최소 6자리 이상이어야 합니다.");
            return false;
        }

        return true;
    }
});
