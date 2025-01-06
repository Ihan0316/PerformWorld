document.addEventListener("DOMContentLoaded", function () {

    const eventsPerPage = 10; // 페이지당 이벤트 개수
    const maxPagesToShow = 10; // 한 번에 표시할 최대 페이지 수
    let currentPage = 1; // 현재 페이지
    let currentPageGroup = 1; // 현재 페이지 그룹 (1부터 시작)
    let totalPages = 0; // 전체 페이지 수
    let allEvents = []; // 모든 이벤트 목록
    const modal = new bootstrap.Modal(document.querySelector(".eventRegisterModal"));
    // 페이지 목록을 로드하는 함수
    loadEvents(currentPage); // 페이지가 로드될 때 첫 번째 페이지의 이벤트 목록을 로드


    // 페이지 목록을 로드하는 함수
    function loadEvents(page) {
        const titleSearch = document.getElementById('titleSearch').value.toLowerCase(); // 제목 검색
        const genreFilter = document.getElementById('genre-code').value; // 장르 선택
        let url = `/event/savedEventList?page=${page}&size=${eventsPerPage}`;

        // 제목이 입력되었으면 title 파라미터 추가
        if (titleSearch) {
            url += `&title=${titleSearch}`;
        }

        // 장르가 선택되었으면 genre 파라미터 추가
        if (genreFilter) {
            url += `&genre=${genreFilter}`;
        }

        console.log("검색 URL:", url); // 생성된 URL을 콘솔에 출력해 확인
        fetch(url, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'  // JSON 응답을 요청
            }
        })
            .then(response => response.json())  // JSON 형식으로 응답 처리
            .then(data => {
                allEvents = data.content; // 필터링된 이벤트 목록 저장
                totalPages = Math.ceil(data.totalElements / eventsPerPage); // 전체 페이지 수 계산
                renderTable(allEvents); // 테이블 렌더링
                printPages(); // 페이지네이션 출력
            })
            .catch(error => console.error('Error:', error));  // 오류 처리
    }

    // 테이블 렌더링 함수
    function renderTable(events) {
        const tbody = document.getElementById('savedEventList');
        tbody.innerHTML = ''; // 기존 테이블 내용 비우기

        events.forEach(event => {
            const row = document.createElement('tr');
            row.innerHTML = `
            <td>${event.eventId}</td>
            <td>${event.title}</td>
            <td>${event.prfpdfrom}</td>
            <td>${event.prfpdto}</td>
            <td>${event.location}</td>
            <td>${event.genre}</td>
            <td><button class="btn btn-primary btn-sm register-btn" >등록</button></td>
        `;
            const regBtn = row.querySelector('button');
            regBtn.addEventListener('click',function (e){
                e.stopPropagation()
                e.preventDefault()
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

                const inputs = document.querySelectorAll('.dynamic-row input');
                inputs.forEach(input => input.value = '');

                // 추가적으로 텍스트나 숫자 필드도 초기화
                const ticketingCounts = document.querySelectorAll('.dynamic-row .ticketing-count');
                ticketingCounts.forEach(count => count.textContent = '');

                modal.show()
            })

            tbody.appendChild(row);  // 테이블에 행 추가
        });
    }



    const pagination = document.querySelector('.pagination');

    // 페이지네이션을 출력하는 함수
    function printPages() {
        let pageStr = '';

        // 페이지 그룹에 해당하는 시작과 끝 페이지 번호
        const startPage = (currentPageGroup - 1) * maxPagesToShow + 1;
        const endPage = Math.min(currentPageGroup * maxPagesToShow, totalPages); // totalPages는 전체 페이지 수

        // '이전' 페이지 그룹 여부
        if (currentPageGroup > 1) {
            pageStr += `
            <li class="page-item"><a class="page-link" data-page="${startPage - maxPagesToShow}">이전</a></li>
        `;
        }

        // 페이지 번호를 출력
        for (let i = startPage; i <= endPage; i++) {
            pageStr += `
            <li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" data-page="${i}">${i}</a>
            </li>
        `;
        }

        // '다음' 페이지 그룹 여부
        if (endPage < totalPages) {
            pageStr += `
            <li class="page-item"><a class="page-link" data-page="${endPage + 1}">다음</a></li>
        `;
        }

        pagination.innerHTML = pageStr;

        // 페이지 링크 클릭 이벤트 추가
        const pageLinks = pagination.querySelectorAll('.page-link');
        pageLinks.forEach(link => {
            link.addEventListener('click', function (e) {
                e.preventDefault();  // 기본 링크 동작 방지
                const page = parseInt(link.getAttribute('data-page'));  // 클릭한 페이지 번호
                if (link.innerText === '이전') {
                    changePageGroup(false);  // '이전' 클릭 시 페이지 그룹 변경
                } else if (link.innerText === '다음') {
                    changePageGroup(true);  // '다음' 클릭 시 페이지 그룹 변경
                } else {
                    if (page !== currentPage) {
                        changePage(page);  // 페이지 변경
                    }
                }
            });
        });
    }

    // 페이지 변경 함수
    function changePage(page) {
        currentPage = page;
        loadEvents(currentPage);  // 페이지가 변경될 때마다 이벤트 목록 다시 가져오기

        // 페이지가 변경되면 맨 위로 스크롤 이동
        window.scrollTo(0, 0);
    }

    // 페이지 그룹을 변경하는 함수 (다음/이전)
    function changePageGroup(isNext) {
        if (isNext && currentPageGroup * maxPagesToShow < totalPages) {
            currentPageGroup += 1;  // 다음 그룹
        } else if (!isNext && currentPageGroup > 1) {
            currentPageGroup -= 1;  // 이전 그룹
        }

        // 그룹을 변경한 후 첫 번째 페이지로 리셋
        currentPage = (currentPageGroup - 1) * maxPagesToShow + 1;

        // 페이지 그룹 변경 후 해당 페이지를 바로 로드
        loadEvents(currentPage);
    }


    // 검색 버튼 클릭 시 실행되는 함수
    document.getElementById('searchButton').addEventListener('click', function() {
        currentPage = 1;  // 검색 시 1페이지로 리셋
        loadEvents(currentPage);  // 검색 조건으로 이벤트 목록을 다시 로드
    });
});

async function getCategoryList() {
    const res = await axios({
        method: 'post',
        url: '/sys/getList',
        data: { mainCode: 'CTG' },  // 장르 카테고리
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
    for(const category of data) {
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
    if(phaseCount==0){
        return
    }
    // 마지막 입력 필드 삭제
    if (container.lastElementChild) {
        container.removeChild(container.lastElementChild);
        phaseCount--; // 차수 감소
    }
});

document.getElementById('saveTicketingButton').addEventListener('click', function () {
    const rows = document.querySelectorAll('.dynamic-row');
    const ticketingData = [];
    let previousEndDate = null; // 이전 티켓팅 종료일
    let isValid = true; // 유효성 검사 플래그
    const eventId = document.getElementById('modal-eventId').textContent; // 모달에서 이벤트 ID 가져오기
    const eventStart = document.getElementById('modal-prfpdfrom').textContent;
    const eventEnd = document.getElementById('modal-prfpdto').textContent;
    const rowsToProcess = Array.from(rows).slice(1);

    function getDateOnly(dateString) {
        const date = new Date(dateString);
        date.setHours(0, 0, 0, 0);  // 시간 부분을 00:00:00으로 설정
        return date;
    }

    rowsToProcess.forEach(row => {

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


        // 유효성 검사 통과 시 ticketingData에 추가
        if (isValid) {
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

    console.log(ticketingData); // 데이터 확인
    if (isValid && ticketingData.length > 0) {
        console.log('서버로 전송되는 데이터:', ticketingData); // 데이터 확인

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
                console.log('티켓팅 데이터 저장 성공:', data);
            })
            .catch(error => {
                alert('티켓팅 저장에 실패했습니다.');
                console.error('티켓팅 저장 실패:', error);
            });
    } else {
        alert('모든 필드를 올바르게 입력해주세요.');
    }
});


