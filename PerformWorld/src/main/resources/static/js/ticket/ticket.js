const initGrid = () => {
    const Grid = tui.Grid;

    // 테마 적용
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

    // 그리드 세팅
    return new Grid({
        el: document.getElementById('ticketingGrid'),
        scrollX: false,
        scrollY: false,
        minBodyHeight: 30,
        pageOptions: {
            useClient: true,  // 클라이언트에서 페이징
            perPage: 10
        },
        draggable: false, // 드래그 비활성화
        columns: [
            {
                header: '선택',
                name: 'select',
                align: 'center',
                width: 50,
                formatter: () => '<input type="checkbox" class="row-checkbox" />'
            },
            {
                header: 'Event ID',
                name: 'eventId',
                align: 'center',
                sortable: true,
                sortingType: 'desc',
                hidden: true
            },
            {
                header: '제목',
                name: 'title',
                align: 'center',
            },
            {
                header: '회차 시작일',
                name: 'prfpdfrom',
                align: 'center',
            },
            {
                header: '회차 종료일',
                name: 'prfpdto',
                align: 'center',
            },
            {
                header: '장소',
                name: 'location',
                align: 'center',
                sortable: true,
                sortingType: 'desc'
            },
            {
                header: '장르',
                name: 'genre',
                align: 'center',
            }
        ]
    });
};

const init = () =>{
    const modal = new bootstrap.Modal(document.querySelector(".eventRegisterModal"));
    const testGrid = initGrid();

    async function loadEvents(){
        const titleSearch = document.getElementById('titleSearch').value.toLowerCase(); // 제목 검색
        const genreFilter = document.getElementById('genre-code').value; // 장르 선택
        let url = `/event/savedEventList?`;

        if (titleSearch) {
            url += `title=${titleSearch}`;
            if(genreFilter){
                url += `&genre=${genreFilter}`;
            }
        } else {
            if(genreFilter){
                url += `genre=${genreFilter}`;
            }
        }

        console.log("검색 URL:", url); // 생성된 URL을 콘솔에 출력해 확인
        try{
            const res = await axios({
                method: 'get',
                url: url,
                header:{
                    'Content-type': 'application/json'
                }
            });
            const ticketData = res.data;
            console.log(ticketData)
            testGrid.resetData(ticketData);
        }catch(error){
            console.error('Error fetching ticketing list:',error);
        }
    }
    loadEvents();

    document.getElementById('searchButton').addEventListener('click', function () {
        loadEvents();  // 검색 조건으로 이벤트 목록을 다시 로드
    });

    async function getCategoryList() {
        const res = await axios({
            method: 'post',
            url: '/sys/getList',
            data: {mainCode: 'CTG'},  // 장르 카테고리
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return res.data;
    }

    getCategoryList().then(data => {
        console.log(data);
        const selectElement = document.querySelector("select[name='genre-code']");
        const optionElement = document.createElement("option");
        optionElement.value = ""
        optionElement.textContent = "장르 선택";  //
        selectElement.appendChild(optionElement);
        for (const category of data) {
            const optionElement = document.createElement("option");
            optionElement.value = category.code;  // 코드
            optionElement.textContent = category.codeName;  // 이름

            selectElement.appendChild(optionElement);
        }
    }).catch(e => {
        console.error(e);
    });

    let phaseCount = 0; // 차수 관리

    // 추가 버튼 클릭 이벤트
    document.getElementById('addRowButton').addEventListener('click', function () {
        phaseCount++;

        const container = document.getElementById('dynamicRowsContainer');

        // 동적 입력 필드 생성
        const row = document.createElement('div');
        row.classList.add('dynamic-row');
        row.setAttribute('data-phase', phaseCount); // 차수 식별을 위한 속성 추가

        row.innerHTML = `
        <div class="cell ticketing-count">${phaseCount}</div>
        <input type="date" name="ticketing-start" class="cell" required />
        <input type="date" name="ticketing-end" class="cell" required />
        <input type="datetime-local" name="open-datetime" class="cell" required />
    `;

        container.appendChild(row); // 컨테이너에 행 추가
    });

// 제거 버튼 클릭 이벤트
    document.getElementById('removeRowButton').addEventListener('click', function () {
        const container = document.getElementById('dynamicRowsContainer');
        if (phaseCount == 0) {
            return
        }
        // 마지막 행을 가져옵니다.
        const lastRow = container.lastElementChild;

        // 마지막 행의 오픈일을 가져옵니다.
        const openDatetimeInput = lastRow.querySelector("input[name='open-datetime']");
        const openDatetime = new Date(openDatetimeInput.value);

        // 현재 시간과 오픈일을 비교
        const currentDate = new Date();
        if (openDatetime <= currentDate) {
            // 오픈일이 지나면 삭제 불가
            alert("이 티켓팅의 오픈일이 지나서 더 이상 삭제할 수 없습니다.");
            return;
        }

        // 오픈일이 지나지 않으면 행을 삭제하고 차수 감소
        container.removeChild(lastRow);
        phaseCount--; // 차수 감소
    });


    document.getElementById('saveTicketingButton').addEventListener('click', function () {
        const rows = document.querySelectorAll('.dynamic-row');
        const ticketingData = [];
        let previousEndDate = null; // 이전 티켓팅 종료일
        let isValid = true; // 유효성 검사 플래그
        const eventId = document.getElementById('modal-eventId').textContent; // 모달에서 이벤트 ID 가져오기
        const eventStart = document.getElementById('modal-prfpdfrom').textContent;
        const eventEnd = document.getElementById('modal-prfpdto').textContent;


        function getDateOnly(dateString) {
            const date = new Date(dateString);
            date.setHours(0, 0, 0, 0);  // 시간 부분을 00:00:00으로 설정
            return date;
        }

        rows.forEach(row => {

            // 각 행에 대해 데이터 추출
            const ticketingCount = row.querySelector('.ticketing-count') ? parseInt(row.querySelector('.ticketing-count').textContent, 10) : 0;  // 숫자로 변환
            const eventPeriodStart = row.querySelector("input[name='ticketing-start']") ? row.querySelector("input[name='ticketing-start']").value : '';
            const eventPeriodEnd = row.querySelector("input[name='ticketing-end']") ? row.querySelector("input[name='ticketing-end']").value : '';
            const openDatetime = row.querySelector("input[name='open-datetime']") ? row.querySelector("input[name='open-datetime']").value : '';

            // 유효성 검사 1: 티켓팅 차수 기간이 작품 전체 기간을 벗어나지 않도록
            if (getDateOnly(eventPeriodStart) < getDateOnly(eventStart) || getDateOnly(eventPeriodEnd) > getDateOnly(eventEnd)) {
                alert('티켓팅 차수의 기간이 작품의 전체 기간을 벗어날 수 없습니다.');
                isValid = false;
            }

            // 유효성 검사 2: 티켓팅 종료일이 다음 티켓팅 시작일보다 뒤에 있어야 함 (동일 날짜는 허용하지 않음)
            if (previousEndDate && getDateOnly(eventPeriodStart) <= getDateOnly(previousEndDate)) {
                alert('이전 티켓팅의 종료일이 다음 티켓팅의 시작일보다 뒤에 있을 수 없습니다. 종료일과 시작일이 동일한 것도 허용되지 않습니다.');
                isValid = false;
            }

            // 유효성 검사 3: 티켓팅 오픈일이 시작일 이전이어야 함
            if (getDateOnly(openDatetime) > getDateOnly(eventPeriodStart)) {
                alert('티켓팅 오픈일은 티켓팅 시작일 이전이어야 합니다.');
                isValid = false;
            }
            // 유효성 검사 1: 티켓팅 차수 기간이 작품 전체 기간을 벗어나지 않도록
            if (getDateOnly(eventPeriodStart) > getDateOnly(eventPeriodEnd)) {
                alert('티켓팅 종료일이 시작일 이전일 수 없습니다.');
                isValid = false;
            }


            // 유효성 검사 통과 시 ticketingData에 추가
            if (isValid && ticketingCount>0) {
                const ticketData = {
                    eventId: eventId,
                    ticketingCount: ticketingCount,
                    eventPeriodStart: eventPeriodStart,
                    eventPeriodEnd: eventPeriodEnd,
                    openDatetime: openDatetime
                };

                ticketingData.push(ticketData);
                previousEndDate = eventPeriodEnd;
            }
        });
        if (isValid) {
            if (ticketingData.length > 0) {
                // 새로운 티켓팅 데이터를 서버로 전송
                fetch(`/ticketing/save`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(ticketingData)
                })
                    .then(response => response.json())
                    .then(data => {
                        alert('티켓팅 정보가 저장되었습니다.');
                    })
                    .catch(error => {
                        alert('티켓팅 저장에 실패했습니다.');
                    });
            } else {
                // 티켓팅 데이터가 없으면 삭제 요청 전송
                fetch(`/ticketing/delete/${eventId}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ eventId: eventId })
                })
                    .then(response => response.json())
                    .then(data => {
                        alert('티켓팅 정보가 삭제되었습니다.');
                    })
                    .catch(error => {
                        alert('티켓팅 삭제에 실패했습니다.');
                    });
            }
        } else {
            alert('모든 필드를 올바르게 입력해주세요.');
        }
    });


    // eventId에 해당하는 티켓팅 데이터를 가져오는 함수
    async function getTicketingData(eventId) {
        try {
            const response = await axios.get(`/ticketing/${eventId}`);
            return response.data;
        } catch (error) {
            console.error("데이터 로드 오류:", error);
        }
    }


    document.getElementById('ticketingRegisterBtn').addEventListener('click', async () => {
        const selectedEventIds = [];
        const checkboxes = document.querySelectorAll('.row-checkbox');
        const rows = testGrid.getData();

        // 선택된 체크박스의 데이터를 가져옴
        checkboxes.forEach((checkbox, index) => {
            if (checkbox.checked) {
                selectedEventIds.push(rows[index]);
            }
        });

        // 선택된 이벤트가 없으면 처리
        if (selectedEventIds.length === 0) {
            alert("선택된 이벤트가 없습니다.");
            return;
        }
        if(selectedEventIds.length > 1){
            alert("한번에 하나의 이벤트만 티켓팅 일정 추가가 가능합니다.");
            return;
        }

        const container = document.getElementById('dynamicRowsContainer');
        // 저장 완료 후 동적 행 초기화 (기존 행 삭제)
        while (container.firstChild) {
            container.removeChild(container.firstChild); // 모든 동적 행 삭제
        }

        // 첫 번째 선택된 이벤트에 대한 상세 정보 설정
        const event = selectedEventIds[0]; // 첫 번째 이벤트의 상세 정보를 가져옵니다.
        console.log(event)
        // 한 줄 요약 설정
        const summary = `${event.title} | ${event.prfpdfrom} ~ ${event.prfpdto} | ${event.location}`;
        document.getElementById('modal-summary').textContent = summary;

        // 상세 데이터 설정
        document.getElementById('modal-eventId').textContent = event.eventId;
        document.getElementById('modal-title').textContent = event.title;
        document.getElementById('modal-prfpdfrom').textContent = event.prfpdfrom;
        document.getElementById('modal-prfpdto').textContent = event.prfpdto;
        document.getElementById('modal-location').textContent = event.location;
        document.getElementById('modal-genre').textContent = event.genre;

        // 동적 입력 필드 초기화
        const inputs = document.querySelectorAll('.dynamic-row input');
        inputs.forEach(input => input.value = '');

        // 선택된 이벤트 ID 콘솔 출력
        console.log("이벤트 ID:", event.eventId);

        try {
            // 티켓팅 데이터 가져오기
            const ticketingData = await getTicketingData(event.eventId);
            phaseCount = 0;  // 데이터를 로드하기 전에 phaseCount를 0으로 초기화

            // 티켓팅 데이터가 있을 경우 기존 행에 이어서 추가
            if (ticketingData && ticketingData.length > 0) {
                console.log("Ticketing Data:", ticketingData); // 데이터를 콘솔에 출력하여 확인

                // 티켓팅 데이터가 있다면 동적 행 생성
                ticketingData.forEach((ticket) => {
                    phaseCount++; // 차수 증가
                    console.log("Ticket:", ticket); // 각 ticket 객체 출력

                    // 동적 행 생성
                    const row = document.createElement('div');
                    row.classList.add('dynamic-row');
                    row.setAttribute('data-phase', phaseCount); // 차수 식별을 위한 속성 추가

                    row.innerHTML = `
                <div class="cell ticketing-count">${phaseCount}</div>
                <input type="date" name="ticketing-start" class="cell" value="${ticket.eventPeriodStart}" required />
                <input type="date" name="ticketing-end" class="cell" value="${ticket.eventPeriodEnd}" required />
                <input type="datetime-local" name="open-datetime" class="cell" value="${ticket.openDatetime}" required />
                <button class="remove-row-button">삭제</button>
            `;

                    container.appendChild(row); // 컨테이너에 행 추가

                    // 날짜가 지나면 수정 불가하도록 설정
                    const openDatetimeInput = row.querySelector("input[name='open-datetime']");
                    const eventPeriodStartInput = row.querySelector("input[name='ticketing-start']");
                    const eventPeriodEndInput = row.querySelector("input[name='ticketing-end']");

                    const openDatetime = new Date(openDatetimeInput.value);
                    const currentDate = new Date();

                    if (openDatetime <= currentDate) {
                        // 날짜가 지나면 입력 필드를 비활성화
                        eventPeriodStartInput.disabled = true;
                        eventPeriodEndInput.disabled = true;
                        openDatetimeInput.disabled = true;
                    }

                    // 삭제 버튼 클릭 이벤트 추가 (행 삭제 기능)
                    row.querySelector('.remove-row-button').addEventListener('click', function () {
                        // 오픈일이 지나면 삭제 불가
                        if (openDatetime <= currentDate) {
                            alert("이 티켓팅의 오픈일이 지나서 더 이상 삭제할 수 없습니다.");
                            return;
                        }

                        // 오픈일이 지나지 않으면 행을 삭제하고 차수 감소
                        container.removeChild(row);
                        phaseCount--; // 차수 감소
                    });
                });
            } else {
                console.log("No ticketing data available");
            }
        } catch (error) {
            console.error("데이터 로드 오류:", error);
        }

        // 모달 표시
        modal.show();
    });




}

window.onload = () =>{
    init();
}
