const initSeatGrid = () => {
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
    const sgrid = new Grid({
        el: document.getElementById('seatGrid'),
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
                header: '좌석 ID',
                name: 'seatId',
                align: 'center',
                minWidth: 150,
                flex: 1,
                sortable: true,
                sortingType: 'asc'
            },
            {
                header: '좌석 등급',
                name: 'section',
                align: 'center',
                minWidth: 150,
                flex: 1
            },
            {
                header: '가격',
                name: 'price',
                align: 'center',
                minWidth: 150,
                flex: 1
            }
        ]
    });

    // resize
    window.addEventListener('resize', function(e) {
        sgrid.refreshLayout();
    });

    return sgrid;
}

const sinit = () => {
    // grid 초기화
    const seatGrid = initSeatGrid();

    // 티어 목록 조회
    async function getSeatList() {
        try {
            const res = await axios({
                method: 'get',
                url: '/admin/getAllSeats',  // 좌석 목록 API 엔드포인트
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            // 그리드에 데이터 설정
            seatGrid.resetData(res.data);
        } catch (error) {
            console.error('Error fetching tier list:', error);
        }
    }

    // 티어 목록 조회 실행
    getSeatList();
}

document.addEventListener("DOMContentLoaded", function () {
    sinit();  // 페이지 DOM이 로드된 후에 그리드 초기화
});