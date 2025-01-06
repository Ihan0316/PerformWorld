const initGrid = () => {
    const Grid = tui.Grid;

    // 테마
    Grid.applyTheme('default',  {
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
    return new Grid({
        el: document.getElementById('grid'),
        scrollX: false,
        scrollY: false,
        minBodyHeight: 30,
        // rowHeaders: ['rowNum'],
        pageOptions: {
            useClient: true,  // 프론트에서 페이징
            perPage: 10
        },
        draggable: false, // 드래그 비활성화
        columns: [
            {
                header: '장르',
                name: 'genre',
                align: 'center',
                width: 30,
                sortable: true,
                sortingType: 'desc'
            },
            {
                header: '제목',
                name: 'eventName',
                align: 'center',
            },
            {
                header: '시작일',
                name: 'eventPeriodStart',
                align: 'center',
            },
            {
                header: '종료일',
                name: 'eventPeriodEnd',
                align: 'center',
            },
            {
                header: '티켓 오픈 일시',
                name: 'openDatetime',
                align: 'center',
                sortable: true,
                sortingType: 'desc'
            }
        ]
    });
}

const init = () => {
    // grid 초기 세팅
    const testGrid = initGrid();

    // 티켓팅 일정 조회
    async function getTicketingList() {
        try {
            const res = await axios({
                method: 'get',
                url: '/ticketing/ticketingInfo',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            // 응답 받은 데이터로 그리드에 표시
            const ticketData = res.data;

            // 그리드에 데이터를 설정
            testGrid.resetData(ticketData);
        } catch (error) {
            console.error('Error fetching ticketing list:', error);
        }
    }
    getTicketingList();

}

window.onload = () => {
    init();
}
