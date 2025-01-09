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

    // 세팅
    return new Grid({
        el: document.getElementById('grid'),
        scrollX: false,
        scrollY: false,
        minBodyHeight: 30,
        // rowHeaders: ['rowNum'],
        pageOptions: {
            useClient: true,  // 프론트에서 페이징
            perPage: 15
        },
        draggable: false, // 드래그 비활성화
        columns: [
            {
                header: '장르',
                name: 'genre',
                align: 'center',
                width: 100,
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
            console.log(ticketData)
            // 그리드에 데이터를 설정
            testGrid.resetData(ticketData);
        } catch (error) {
            console.error('Error fetching ticketing list:', error);
        }
    }
    getTicketingList();

}

document.querySelector(".ticketingRegBtn").addEventListener("click",function (e){
    e.preventDefault()
    e.stopPropagation()

    window.location.href="/ticketing/register";
})

window.onload = () => {
    init();
}
