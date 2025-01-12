const initUserGrid = () => {
    const Grid = tui.Grid;

    // 테마
    Grid.applyTheme('clean', {
        cell: {
            normal: {
                border: 'gray',
                showVerticalBorder: true,
                showHorizontalBorder: true
            },
            header: {
                background: 'gray',
                text: 'white',
                border: 'white'
            }
        }
    });

    // 그리드 설정
    const userGrid = new Grid({
        el: document.getElementById('userGrid'),
        scrollX: false,
        scrollY: false,
        minBodyHeight: 30,
        pageOptions: {
            useClient: true,
            perPage: 5
        },
        draggable: false,
        columns: [
            {
                header: '선택',
                name: 'select',
                align: 'center',
                width: 50,
                formatter: () => '<input type="checkbox" class="row-checkbox" />'
            },
            { header: '유저 아이디', name: 'userId', align: 'center', minWidth: 150, flex: 1, sortable: true, sortingType: 'asc' },
            { header: '이름', name: 'name', align: 'center', minWidth: 150, flex: 1 },
            { header: '이메일', name: 'email', align: 'center', minWidth: 200, flex: 2 },
            { header: '전화번호', name: 'phoneNumber', align: 'center', minWidth: 150, flex: 1 },
            {
                header: '주소',
                name: 'fullAddress', // 가상의 이름
                align: 'center',
                minWidth: 400,
                flex: 10,
                formatter: ({ row }) => {
                    const address1 = row.address1 || ''; // 기본 주소
                    const address2 = row.address2 || ''; // 상세 주소
                    const postcode = row.postcode || ''; // 우편번호
                    return `${address1} ${address2} (${postcode})`.trim(); // 결합된 주소
                }
            },
            { header: '등급', name: 'tierName', align: 'center', minWidth: 100, flex: 1 }
        ]
    });

    // 리사이즈 이벤트 처리
    window.addEventListener('resize', () => {
        userGrid.refreshLayout();
    });

    return userGrid;
};

const getUserList = async (userGrid) => {
    try {
        const res = await axios({
            method: 'get',
            url: '/admin/getAllUsers',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        const userData = res.data.filter(resuser => resuser.userId !== user.uid); // admin 계정 제외
        userGrid.resetData(userData); // 데이터를 그리드에 바인딩
    } catch (error) {
        console.error('Error fetching user list:', error);
        alert('사용자 목록을 가져오는 데 오류가 발생했습니다.');
    }
};

const deleteUsers = async (selectedUserIds) => {
    if (selectedUserIds.length === 0) {
        alert('삭제할 사용자를 선택해주세요.');
        return;
    }

    if (!confirm('선택한 사용자를 삭제하시겠습니까?')) {
        return;
    }

    for (const userId of selectedUserIds) {
        try {
            const response = await axios.delete(`/admin/user/${userId}`);
            if (response.status === 200) {
                console.log(`사용자 ${userId} 삭제 성공`);
            } else {
                console.error(`사용자 ${userId} 삭제 실패`);
            }
        } catch (error) {
            console.error(`사용자 ${userId} 삭제 중 오류 발생:`, error);
        }
    }

    alert('사용자 삭제 작업이 완료되었습니다.');
    window.location.reload(); // 페이지 새로고침
};

const uinit = () => {
    const userGrid = initUserGrid();
    getUserList(userGrid); // 사용자 목록 조회 실행

    // userIDBtn 클릭 이벤트 추가
    document.getElementById('userIDBtn').addEventListener('click', () => {
        const selectedUserIds = [];
        const checkboxes = document.querySelectorAll('.row-checkbox');
        const rows = userGrid.getData();

        // 선택된 체크박스의 데이터를 가져옴
        checkboxes.forEach((checkbox, index) => {
            if (checkbox.checked) {
                selectedUserIds.push(rows[index].userId);
            }
        });

        // 선택된 사용자 삭제 함수 호출
        deleteUsers(selectedUserIds);
    });
};

document.addEventListener('DOMContentLoaded', function () {
    uinit(); // 페이지 DOM이 로드된 후 초기화 실행
});