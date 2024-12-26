const init = () => {

    const chnPwModal = new bootstrap.Modal(document.querySelector('.chnPwModal'));

    // data 가져오기
    // getUserInfo().then(data => {
    //     document.querySelector("input[name='userId']").value = data.userId;
    //     document.querySelector("input[name='name']").value = data.name;
    //     document.querySelector("input[name='tierName']").value = data.tier.tierName;
    //     document.querySelector("input[name='email']").value = data.email;
    //     document.querySelector("input[name='phoneNumber']").value = data.phoneNumber;
    //     document.querySelector("input[name='address1']").value = data.address1;
    //     document.querySelector("input[name='address2']").value = data.address2;
    //     document.querySelector("input[name='postcode']").value = data.postcode;
    //
    // }).catch(e => {
    //    console.error("회원 정보를 불러오는데 실패했습니다.");
    // });

    // 비밀번호 변경
    document.querySelector(".chnPwBtn").addEventListener("click", function (e) {
        // 모달 open
        chnPwModal.show();
        // 새 비밀번호 입력 방식
    });

    // 정보 수정
    document.querySelector(".updUserBtn").addEventListener("click", function (e) {
        if(e.target.value === '정보 수정') {
            modeChange(1);
            e.target.value = '수정 완료';

        } else {
            if(confirm("회원 정보를 수정하시겠습니까?")) {
                // axios
                modeChange(0);
                e.target.value = '정보 수정';
            }
        }
    });

    // 주소 변경
    document.querySelectorAll("input[name^='address']").forEach(function(input) {
        input.addEventListener("click", function (e) {
            openPostCode();
        });
    });

    // 회원 탈퇴
    document.querySelector(".delUserBtn").addEventListener("click", function (e) {
        if(confirm("정말로 탈퇴하시겠습니까?")) {
            // axios
        }
    });



    // 폼 mode 변경
    const modeChange = (mode) => {
        if(mode === 1) {
            document.querySelector("input[name='name']").disabled = false;
            document.querySelector("input[name='email']").disabled = false;
            document.querySelector("input[name='phoneNumber']").disabled = false;
            document.querySelector("input[name='address1']").disabled = false;
            document.querySelector("input[name='address1']").readOnly = true;
            document.querySelector("input[name='address2']").disabled = false;
        } else {
            document.querySelector("input[name='name']").disabled = true;
            document.querySelector("input[name='email']").disabled = true;
            document.querySelector("input[name='phoneNumber']").disabled = true;
            document.querySelector("input[name='address1']").disabled = true;
            document.querySelector("input[name='address1']").readOnly = false;
            document.querySelector("input[name='address2']").disabled = true;
        }
    }

    // 내 정보 조회
    async function getUserInfo() {
        const loginInfo = sessionStorage.getItem("loginInfo");

        const res = await axios({
            method : 'post',
            url : '/user/getInfo',
            data : loginInfo,
            headers : {
                'Content-Type' : 'application/json'
            }
        });
        return res.data;
    }

    // 주소 api
    const openPostCode = () => {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                if(data.userSelectedType === 'R'){
                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가한다.
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    if(extraAddr !== ''){
                        extraAddr = ' (' + extraAddr + ')';
                    }
                    // 조합된 참고항목을 해당 필드에 넣는다.
                    //document.getElementById("sample6_extraAddress").value = extraAddr;

                } else {
                    //document.getElementById("sample6_extraAddress").value = '';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.querySelector("input[name='postcode']").value = data.zonecode;
                document.querySelector("input[name='address1']").value = addr;
                // 커서를 상세주소 필드로 이동한다.
                document.querySelector("input[name='address2']").focus();
            }
        }).open();
    }
}

window.onload = () => {
    init();
}