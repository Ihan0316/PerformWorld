$(document).ready(function () {
    $("#loginForm").submit(function (e) {

        var username = $("input[name='username']").val();
        var password = $("input[name='password']").val();

        if (!validateForm(username, password)) {
            return;
        }

        $("#loginButton").prop("disabled", true).val("로그인 중...");
        $("#loadingSpinner").show();

        $.ajax({
            url: '/user/login',
            type: 'POST',
            data: {
                username: username,
                password: password
            },
            success: function (response) {
                alert("로그인 성공!");

                sessionStorage.setItem('currentUser', response.username);
            },
            error: function (xhr, status, error) {
                alert(xhr.responseText || "로그인 실패");
            },
            complete: function () {
                $("#loginButton").prop("disabled", false).val("Login");
                $("#loadingSpinner").hide();
            }
        });
    });

    function validateForm(username, password) {
        if (!username || !password) {
            alert("아이디와 비밀번호를 모두 입력해주세요.");
            return false;
        }

        if (password.length < 6) {
            alert("비밀번호는 최소 6자리 이상이어야 합니다.");
            return false;
        }

        return true;
    }

    // 비밀번호 찾기 모달의 폼 제출을 AJAX로 처리
    document.getElementById('findPwForm').addEventListener('submit', function(event) {
        event.preventDefault();  // 폼 제출을 기본 방식으로 처리하지 않도록 함
        const email = document.getElementById('email').value;

        // AJAX 요청 보내기
        fetch('/user/findPw', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({email: encodeURIComponent(email)})  // JSON으로 변환하여 요청 본문에 전달
        })
            .then(response => response.json())  // 서버로부터 JSON 응답 받기
            .then(data => {
                alert(data.message);  // 서버에서 받은 메시지 출력
            })
            .catch(error => {
                alert('서버 오류: ' + error);
            });
    });
});
