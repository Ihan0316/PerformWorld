document.addEventListener("DOMContentLoaded", function () {
    const Grid = tui.Grid;

    // ToastUI Grid 테마 설정
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

    // ToastUI Grid 초기화
    const grid = new Grid({
        el: document.getElementById('eventListGrid'),
        scrollX: false, // 가로 스크롤 비활성화
        scrollY: false, // 세로 스크롤 비활성화
        minBodyHeight: 400, // 최소 높이 설정
        pageOptions: {
            useClient: true,
            perPage: 10
        },
        columns: [
            {
                header: '선택',
                name: 'select',
                align: 'center',
                width: 50,
                formatter: () => '<input type="checkbox" class="row-checkbox" />'
            },
            {
                header: '썸네일',
                name: 'thumbnailUrl',
                align: 'center',
                width: 100,
                formatter: ({ value }) => `<img src="${value}" alt="썸네일" style=" height: 31px;">`
            },
            { header: '이벤트 ID', name: 'eventId', align: 'center', minWidth: 10, hidden: true },
            { header: '제목', name: 'title', align: 'center', minWidth: 250 },
            { header: '시작일', name: 'prfpdfrom', align: 'center', minWidth: 120 },
            { header: '종료일', name: 'prfpdto', align: 'center', minWidth: 120 },
            { header: '위치', name: 'location', align: 'center', minWidth: 150 },
            { header: '장르', name: 'genre', align: 'center', minWidth: 100 }
        ],
        data: [] // 초기 데이터 비어있음
    });

    // 리사이즈 이벤트 처리
    window.addEventListener('resize', () => {
        grid.refreshLayout(); // 그리드 레이아웃 갱신
    });

    // 이벤트 목록 로드 함수
    const loadEvents = async () => {
        const titleSearch = document.getElementById('titleSearch').value.toLowerCase();
        const genreFilter = document.getElementById('genre-code').value;
        let url = '/event/savedEventList';

        // 조건에 따른 URL 파라미터 추가
        if (titleSearch) url += `?title=${titleSearch}`;
        if (genreFilter) url += titleSearch ? `&genre=${genreFilter}` : `?genre=${genreFilter}`;

        try {
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                }
            });
            const data = await response.json();
            const events = data || []; // 응답 데이터가 없을 경우 빈 배열로 처리
            grid.resetData(events); // 그리드 데이터 갱신
            grid.refreshLayout(); // 데이터 추가 후 그리드 레이아웃 갱신
        } catch (error) {
            console.error('Error loading events:', error);
            alert('이벤트 목록을 불러오는 중 오류가 발생했습니다.');
        }
    };

    // 선택된 이벤트 삭제 함수
    const deleteSelectedEvents = async (selectedEventIds) => {
        if (selectedEventIds.length === 0) {
            alert('삭제할 이벤트를 선택해주세요.');
            return;
        }

        if (!confirm('선택한 이벤트를 삭제하시겠습니까?')) {
            return;
        }

        try {
            const response = await fetch('/event/deleteEvents', {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(selectedEventIds) // 선택된 이벤트 ID를 JSON 형식으로 전송
            });

            if (response.ok) {
                console.log('이벤트 삭제 성공');
                alert('선택한 이벤트가 삭제되었습니다.');
                loadEvents(); // 삭제 후 이벤트 목록 새로 고침
            } else {
                console.error('이벤트 삭제 실패');
                alert('이벤트 삭제에 실패했습니다.');
            }
        } catch (error) {
            console.error('이벤트 삭제 중 오류 발생:', error);
            alert('이벤트 삭제 중 오류가 발생했습니다.');
        }
    };


    // 검색 버튼 이벤트
    document.getElementById('searchButton').addEventListener('click', loadEvents);

    // 삭제 버튼 이벤트
    document.getElementById('eventDeleteBtn').addEventListener('click', () => {
        const selectedEventIds = [];
        const checkboxes = document.querySelectorAll('.row-checkbox');
        const rows = grid.getData();

        // 선택된 체크박스의 데이터를 가져옴
        checkboxes.forEach((checkbox, index) => {
            if (checkbox.checked) {
                selectedEventIds.push(rows[index].eventId);
            }
        });

        // 선택된 이벤트 삭제 함수 호출
        deleteSelectedEvents(selectedEventIds);
    });

    // 페이지 로드 시 이벤트 목록 초기화
    loadEvents();
});
