// grid 초기화
const initBkGrid = () => {
    const BookGrid = tui.Grid;

    // 테마
    BookGrid.applyTheme('default',  {
        cell: {
            normal: {
                border: 'gray'
            },
            header: {
                background: 'gray',
                text: 'white',
                border: 'gray'
            },
            rowHeaders: {
                header: {
                    background: 'gray',
                    text: 'white'
                }
            }
        }
    });

    // 세팅
    return new BookGrid({
        el: document.getElementById('bookingGrid'),
        scrollX: false,
        scrollY: false,
        minBodyHeight: 150,
        rowHeaders: ['rowNum'],
        pageOptions: {
            useClient: true,  // 프론트에서 페이징
            perPage: 5
        },
        columns: [
            {
                header: '번호',
                name: 'bookingId',
                hidden: true
            },
            {
                header: '공연명',
                name: 'title',
                align: 'center'
            },
            {
                header: '공연장',
                name: 'location',
                align: 'center'
            },
            {
                header: '예매상태',
                name: 'bookingStatus',
                align: 'center'
            },
            {
                header: '관람일',
                name: 'eventDate',
                formatter: (value) => {
                    if (value) {
                        const data = value.value;
                        return `${data[0]}-${data[1]}-${data[2]} ${data[3]}:${data[4]}`;
                    }
                    return "";
                }
            },
            {
                header: '캐스팅',
                name: 'eventCast',
                align: 'center',
                // formatter: (value) => {
                //     if (value) {
                //         const data = value.value;
                //         return `${data ? '완료' : '취소'}`;
                //     }
                //     return "";
                // }
            },
            {
                header: '좌석정보',
                name: 'seat',
                // formatter: (value) => {
                //     if (value) {
                //         const data = value.value;
                //         return `${data[0]}-${data[1]}-${data[2]}`;
                //     }
                //     return "";
                // }
            },
            {
                header: '금액',
                name: 'totalPrice',
                align: 'center'
            },
            {
                header: '예매일',
                name: 'regDate',
                formatter: (value) => {
                    if (value) {
                        const data = value.value;
                        return `${data[0]}-${data[1]}-${data[2]}`;
                    }
                    return "";
                }
            }
        ]
    });
}
const initRvGrid = () => {
    const ReviewGrid = tui.Grid;

    // 테마
    ReviewGrid.applyTheme('default',  {
        cell: {
            normal: {
                border: 'gray'
            },
            header: {
                background: 'gray',
                text: 'white',
                border: 'gray'
            },
            rowHeaders: {
                header: {
                    background: 'gray',
                    text: 'white'
                }
            }
        }
    });

    // 세팅
    return new ReviewGrid({
        el: document.getElementById('reviewGrid'),
        scrollX: false,
        scrollY: false,
        minBodyHeight: 150,
        rowHeaders: ['rowNum'],
        pageOptions: {
            useClient: true,  // 프론트에서 페이징
            perPage: 4
        },
        columns: [
            {
                header: '번호',
                name: 'reviewId',
                hidden: true
            },
            {
                header: '공연명',
                name: 'title',
                align: 'center'
            },
            {
                header: '관람일',
                name: 'eventDate',
                formatter: (value) => {
                    if (value) {
                        const data = value.value;
                        return `${data[0]}-${data[1]}-${data[2]} ${data[3]}:${data[4]}`;
                    }
                    return "";
                }
            },
            {
                header: '내용',
                name: 'content'
            },
            {
                header: '작성일',
                name: 'regDate',
                formatter: (value) => {
                    if (value) {
                        const data = value.value;
                        return `${data[0]}-${data[1]}-${data[2]}`;
                    }
                    return "";
                }
            }
        ]
    });
}

const init = () => {
     getUserInfo();

    // grid 초기 세팅
    const bookingGrid = initBkGrid();
    // getBkList().then(res => {
    //     bookingGrid.resetData(res.data);  // grid에 세팅
    // }).catch(e => {
    //     alert("예매내역을 불러오는데 실패했습니다.");
    // })
    const reviewGrid = initRvGrid();
    // getRvList().then(res => {
    //     reviewGrid.resetData(res.data);  // grid에 세팅
    // }).catch(e => {
    //     alert("후기목록을 불러오는데 실패했습니다.");
    // })

    // 비밀번호 변경
    document.querySelector(".chnPwBtn").addEventListener("click", function (e) {
        chnPwModal.show();
    });
    document.querySelector(".updPwBtn").addEventListener("click", function (e) {
        if(document.querySelector("input[name='password']").value === "") {
            alert("비밀번호를 입력해주세요.");
            return;
        }
        if(document.querySelector("input[name='password']").value !==
            document.querySelector("input[name='chkPassword']").value) {
            alert("비밀번호를 다시 확인해주세요.");
            return;
        }

        if(confirm("비밀번호를 변경하시겠습니까?")) {
            updUserPw().then(res => {
                alert("비밀번호 변경에 성공했습니다.");
                chnPwModal.hide();
            }).catch(e => {
                alert("비밀번호 수정에 실패했습니다.");
            });
        }
    });

    // 정보 수정
    document.querySelector(".updUserBtn").addEventListener("click", function (e) {
        if(e.target.value === '정보 수정') {
            modeChange(1);
            e.target.value = '수정 완료';

        } else {
            const form = document.querySelector('form');
            if (!form.checkValidity()) {
                alert("모든 필드를 올바르게 입력해주세요.");
                return;
            }

            if(confirm("회원 정보를 수정하시겠습니까?")) {
                updUserInfo().then(res => {
                    alert("회원정보 수정에 성공했습니다.");
                    getUserInfo();
                    modeChange(0);
                    e.target.value = '정보 수정';
                }).catch(e => {
                    alert("회원정보 수정에 실패했습니다.");
                });
            }
        }
    });

    // 주소 변경
    document.querySelector("input[name='address1']").addEventListener("click", function (e) {
        openPostCode();
    });

    // 회원 탈퇴
    document.querySelector(".delUserBtn").addEventListener("click", function (e) {
        if(confirm("정말로 탈퇴하시겠습니까?")) {
            delUserInfo().then(res => {
                alert("회원 탈퇴에 성공했습니다.");
                window.location.href = '/';
            }).catch(e => {
                alert("회원 탈퇴에 실패했습니다.");
            });
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

    // 정보 조회
    async function getUserInfo() {
        try {
            const res = await axios({
                method: 'post',
                url: '/user/getInfo',
                data: { userId: 'user123' }, // loginInfo
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            document.querySelector("input[name='userId']").value = res.data.userId;
            document.querySelector("input[name='name']").value = res.data.name;
            document.querySelector("input[name='tierName']").value = res.data.tierName;
            document.querySelector("input[name='email']").value = res.data.email;
            document.querySelector("input[name='phoneNumber']").value = res.data.phoneNumber;
            document.querySelector("input[name='address1']").value = res.data.address1;
            document.querySelector("input[name='address2']").value = res.data.address2;
            document.querySelector("input[name='postcode']").value = res.data.postcode;

        } catch (error) {
            alert("회원 정보를 불러오는데 실패했습니다.");
        }
    }

    // 비밀번호 변경
    async function updUserPw() {
        const user = {
            userId: document.querySelector("input[name='userId']").value,
            password: document.querySelector("input[name='password']").value,
        };

        const res = await axios({
            method : 'post',
            url : '/user/changePw',
            data : user,
            headers : {
                'Content-Type' : 'application/json'
            }
        });
        return res.data;
    }

    // 정보 수정
    async function updUserInfo() {
        const user = {
            userId: document.querySelector("input[name='userId']").value,
            name: document.querySelector("input[name='name']").value,
            email: document.querySelector("input[name='email']").value,
            phoneNumber: document.querySelector("input[name='phoneNumber']").value,
            address1: document.querySelector("input[name='address1']").value,
            address2: document.querySelector("input[name='address2']").value,
            postcode: document.querySelector("input[name='postcode']").value,
        };

        const res = await axios({
            method : 'put',
            url : '/user',
            data : user,
           headers : {
               'Content-Type' : 'application/json'
           }
        });
        return res.data;
    }

    // 회원 탈퇴
    async function delUserInfo() {
        const res = await axios({
            method : 'delete',
            url : '/user',
            data : { userId: 'user123' },  // loginInfo
            headers : {
                'Content-Type' : 'application/json'
            }
        });
        return res.data;
    }

    // 예매내역 조회
    async function getBkList() {
        const res = await axios({
            method : 'post',
            url : '/book/getBknList',
            data : { userId: 'user123' },  // loginInfo
            headers : {
                'Content-Type' : 'application/json'
            }
        });
        return res.data;
    }

    // 후기목록 조회
    async function getRvList() {
        const res = await axios({
            method : 'post',
            url : '/review/getRvList',
            data : { userId: 'user123' },  // loginInfo
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