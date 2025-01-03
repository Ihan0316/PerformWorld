document.addEventListener("DOMContentLoaded", function () {

    const eventsPerPage = 10; // 페이지당 이벤트 개수
    const maxPagesToShow = 10; // 한 번에 표시할 최대 페이지 수
    let currentPage = 1; // 현재 페이지
    let currentPageGroup = 1; // 현재 페이지 그룹 (1부터 시작)
    let totalPages = 0; // 전체 페이지 수
    let allEvents = []; // 모든 이벤트 목록

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
            <td><button class="btn btn-primary btn-sm" data-event-id="${event.eventId}">등록</button></td>
        `;

            // 삭제 버튼에 클릭 이벤트 추가
            const deleteButton = row.querySelector('button');
            deleteButton.addEventListener('click', function () {
                const eventId = deleteButton.getAttribute('data-event-id');
                deleteEvent(eventId);
            });

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

    // 삭제 버튼 클릭 시 실행되는 함수
    function deleteEvent(eventId) {
        const isConfirmed = window.confirm("정말 이 이벤트를 삭제하시겠습니까?");
        if (isConfirmed) {
            fetch(`/event/deleteEvent/${eventId}`, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json'
                }
            })
                .then(response => {
                    if (response.ok) {
                        alert('이벤트가 삭제되었습니다.');
                        loadEvents(currentPage);  // 삭제 후 현재 페이지의 이벤트 목록 다시 로드
                    } else {
                        alert('이벤트 삭제에 실패했습니다.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('이벤트 삭제 중 오류가 발생했습니다.');
                });
        }
    }

    // 검색 버튼 클릭 시 실행되는 함수
    document.getElementById('searchButton').addEventListener('click', function() {
        currentPage = 1;  // 검색 시 1페이지로 리셋
        loadEvents(currentPage);  // 검색 조건으로 이벤트 목록을 다시 로드
    });
});

    async function getCategoryList2() {
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
    getCategoryList2().then(data => {
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
