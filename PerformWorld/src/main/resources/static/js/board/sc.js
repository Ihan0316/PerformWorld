// 탭 전환 기능
function switchTab(tabId) {
    // 경로 변경
    history.pushState(null, null, `/faq/list/${tabId}`);

    // 모든 탭 콘텐츠 숨기기
    const tabs = document.querySelectorAll('.tab-content');
    tabs.forEach(tab => tab.style.display = 'none');

    // 모든 탭 메뉴에서 active 클래스 제거
    const menuItems = document.querySelectorAll('.header_genreMenu__34716 li');
    menuItems.forEach(item => item.classList.remove('active'));

    // 선택된 탭 표시
    const activeTab = document.getElementById(tabId);
    if (activeTab) {
        activeTab.style.display = 'block';
    }

    // 선택된 탭 메뉴 항목에 active 클래스 추가
    const activeMenuItem = document.querySelector(`#${tabId}-link`);
    if (activeMenuItem) {
        activeMenuItem.classList.add('active');
    }
}

// 기본 탭 활성화 및 URL 경로에 따른 탭 활성화
document.addEventListener('DOMContentLoaded', () => {
    const path = window.location.pathname;
    const tabId = path.split('/').pop(); // "notice-tab" 추출
    switchTab(tabId || 'notice-tab');    // 기본값을 'notice-tab'으로 설정
});

// 검색 이벤트 핸들러
function handleSearch(event) {
    if (event.key === 'Enter') {
        alert('검색어: ' + event.target.value);
    }
}

// 페이지 로드 함수
function loadPage(pageNum) {
    // 요청할 URL을 서버의 페이징된 데이터 API로 설정
    fetch(`/faq/list?noticePage=${pageNum}`)
        .then(response => response.json()) // JSON 응답 처리
        .then(data => {
            updateTable(data.notices);
            updatePagination(data.page);
        })
        .catch(error => console.error('Error fetching page data:', error));
}

// 테이블 갱신 함수
function updateTable(notices) {
    const tableBody = document.querySelector("#notices tbody");
    tableBody.innerHTML = ""; // 기존 테이블 내용 삭제
    notices.forEach(notice => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${notice.noticeId}</td>
            <td>${notice.title}</td>
            <td>${notice.content}</td>
            <td>${notice.createdAt}</td>
            <td>${notice.updatedAt}</td>
        `;
        tableBody.appendChild(row);
    });
}

// 페이징 정보 갱신 함수
function updatePagination(page) {
    const pagination = document.querySelector(".pagination");
    pagination.innerHTML = ""; // 기존 페이징 내용 삭제

    if (page.hasPrevious) {
        pagination.innerHTML += `<a href="javascript:void(0)" onclick="loadPage(${page.number - 1})">이전</a>`;
    }
    pagination.innerHTML += `<span>${page.number + 1}</span>`;
    if (page.hasNext) {
        pagination.innerHTML += `<a href="javascript:void(0)" onclick="loadPage(${page.number + 1})">다음</a>`;
    }
}

// 탭 전환 함수
function switchTab(tabId) {
    document.querySelectorAll('.tab-content').forEach(function(tab) {
        tab.style.display = 'none';
    });
    document.getElementById(tabId).style.display = 'block';
}
