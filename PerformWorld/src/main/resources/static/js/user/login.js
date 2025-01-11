$(document).ready(function () {
    // 로그인 폼 제출 시
    // $("#loginForm").submit(function (e) {
    //     e.preventDefault();  // 기본 폼 제출 동작을 막음
    //
    //     var username = $("input[name='username']").val();
    //     var password = $("input[name='password']").val();
    //
    //     if (!validateForm(username, password)) {
    //         return;
    //     }
    //
    //     $("#loginButton").prop("disabled", true).val("로그인 중...");
    //     $("#loadingSpinner").show();
    //
    //     // 로그인 요청을 AJAX로 보내기
    //     $.ajax({
    //         url: '/user/login',
    //         type: 'POST',
    //         data: {
    //             username: username,
    //             password: password
    //         },
    //         success: function (response) {
    //             console.log(response)
    //             // alert("로그인 성공!");
    //
    //             // // 세션에 사용자 정보 저장
    //             // sessionStorage.setItem('currentUser', response.username);
    //             //
    //             // // 로그인 후 리다이렉션
    //             // window.location.href = "/";  // 홈 페이지로 리다이렉션
    //         },
    //         error: function (xhr, status, error) {
    //             alert(xhr.responseText || "로그인 실패");
    //         },
    //         complete: function () {
    //             $("#loginButton").prop("disabled", false).val("Login");
    //             $("#loadingSpinner").hide();
    //         }
    //     });
    // });

    // 로그인 폼 유효성 검사
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
        fetch('/user/resetPw', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({email:email})  // JSON으로 변환하여 요청 본문에 전달
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
