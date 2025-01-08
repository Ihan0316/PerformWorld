const initGrid = () => {
    const Grid = tui.Grid;

    // 테마
    Grid.applyTheme('clean',  {
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
    const ugrid = new Grid({
        el: document.getElementById('userGrid'),
        scrollX: false,
        scrollY: false,
        minBodyHeight: 30,
        pageOptions: {
            useClient: true,  // 프론트에서 페이징
            perPage: 10
        },
        draggable: false, // 드래그 비활성화
        columns: [
            {
                header: '유저 아이디',
                name: 'userId',
                align: 'center',
                minWidth: 150,
                flex: 1,  // 비율에 따라 크기 조정
                sortable: true,
                sortingType: 'asc'
            },
            {
                header: '이름',
                name: 'name',
                align: 'center',
                minWidth: 150,
                flex: 1
            },
            {
                header: '이메일',
                name: 'email',
                align: 'center',
                minWidth: 200,
                flex: 2
            },
            {
                header: '전화번호',
                name: 'phoneNumber',
                align: 'center',
                minWidth: 150,
                flex: 1
            },
            {
                header: '주소',
                name: 'address1',
                align: 'center',
                minWidth: 200,
                flex: 2
            },
            {
                header: '상세주소',
                name: 'address2',
                align: 'center',
                minWidth: 150,
                flex: 2
            },
            {
                header: '우편번호',
                name: 'postcode',
                align: 'center',
                minWidth: 100,
                flex: 1
            },
            {
                header: '티어',
                name: 'tierName',
                align: 'center',
                minWidth: 100,
                flex: 1
            }
        ]
    });

    // resize
    window.addEventListener('resize', function(e) {
        ugrid.refreshLayout();
    });

    return ugrid;
}

const init = () => {
    // grid 초기화
    const userGrid = initGrid();

    // 사용자 목록 조회
    async function getUserList() {
        try {
            const res = await axios({
                method: 'get',
                url: '/admin/getAllUsers',  // 사용자 목록 API 엔드포인트
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            // 응답 받은 데이터에서 'userId'가 'admin'인 사용자는 제외
            const userData = res.data.filter(user => user.userId !== 'admin');

            // 그리드에 데이터 설정
            userGrid.resetData(userData);
        } catch (error) {
            console.error('Error fetching user list:', error);
        }
    }

    // 사용자 목록 조회 실행
    getUserList();
}

document.addEventListener("DOMContentLoaded", function () {
    init();  // 페이지 DOM이 로드된 후에 그리드 초기화
});