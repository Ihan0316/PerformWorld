// 폼 유효성 검사 함수
function validateForm() {
    const email = document.querySelector("input[name='email']").value;
    const password = document.querySelector("input[name='password']").value;
    const confirmPassword = document.querySelector("input[name='confirmPassword']").value;
    const name = document.querySelector("input[name='name']").value;
    const phoneNumber = document.querySelector("input[name='phoneNumber']").value;  // 전화번호 값 가져오기

    // 이메일 유효성 검사 (간단한 정규식 사용)
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    if (!emailRegex.test(email)) {
        alert("유효한 이메일을 입력해주세요.");
        return false;
    }

    // 비밀번호 일치 검사
    if (password !== confirmPassword) {
        document.querySelector("#passwordError").textContent = "비밀번호가 일치하지 않습니다.";
        return false;
    } else {
        document.querySelector("#passwordError").textContent = "";
    }

    // 이름이 비어있는지 확인
    if (!name) {
        alert("이름을 입력해주세요.");
        return false;
    }

    // 전화번호 유효성 검사 (010-xxxx-xxxx 형식)
    const phoneRegex = /^(010-\d{4}-\d{4})$/; // 010-xxxx-xxxx 형식
    if (!phoneRegex.test(phoneNumber)) {
        document.querySelector("#phoneError").textContent = "유효한 전화번호를 입력해주세요. 형식: 010-1234-5678";
        return false;
    } else {
        document.querySelector("#phoneError").textContent = "";
    }

    // 모든 검사를 통과한 경우
    return true;
}

// 폼 제출 처리 (AJAX)
function submitForm(event) {
    event.preventDefault(); // 폼의 기본 제출 동작 방지

    // 폼 데이터 가져오기
    const formData = {
        email: document.querySelector("input[name='email']").value,
        name: document.querySelector("input[name='name']").value,
        userId: document.querySelector("input[name='userId']").value,
        password: document.querySelector("input[name='password']").value,
        confirmPassword: document.querySelector("input[name='confirmPassword']").value,
        phoneNumber: document.querySelector("input[name='phoneNumber']").value,
        postcode: document.querySelector("input[name='postcode']").value,
        address1: document.querySelector("input[name='address1']").value,
        address2: document.querySelector("input[name='address2']").value
    };
    // 유효성 검사
    if (!validateForm()) {
        return;
    }

    // AJAX 요청 (fetch API 사용)
    fetch('/user/join', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(formData)  // JSON으로 변환하여 요청 본문에 전달
    })
        .then(response => response.json())  // JSON 응답을 받음
        .then(data => {
            if (data.result === "success") {
                alert("회원가입이 완료되었습니다.");
                window.location.href = '/user/login'; // 로그인 페이지로 리다이렉트
            } else if (data.error === "uid") {
                alert("이미 사용 중인 아이디입니다.");
            }
        })
        .catch(error => {
            alert("서버 오류가 발생했습니다.");
            console.error("Error:", error);
        });
}

// 주소 찾기 함수 (다음 주소 API)
const openPostCode = () => {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            // 도로명 주소를 우선으로 사용
            if (data.userSelectedType === 'R') {
                addr = data.roadAddress; // 도로명 주소
            } else {
                addr = data.jibunAddress; // 지번 주소
            }

            // 도로명 주소에 대한 추가 정보 처리
            if (data.userSelectedType === 'R') {
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if (extraAddr !== '') {
                    extraAddr = ' (' + extraAddr + ')';
                }
            }

            // 우편번호, 주소1, 주소2 입력값 설정
            document.querySelector("input[name='postcode']").value = data.zonecode; // 우편번호
            document.querySelector("input[name='address1']").value = addr + extraAddr; // 도로명 주소 + 참고 항목
            document.querySelector("input[name='address2']").focus(); // 상세 주소 입력란 포커스
        }
    }).open();
}
