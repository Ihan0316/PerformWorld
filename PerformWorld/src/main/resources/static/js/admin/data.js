// 전역 변수 설정
let currentPage = 0;
const pageSize = 10;
let totalPages = 0;  // 전체 페이지 수

// 페이지 로딩 시 호출되는 함수
window.onload = function() {
    // 처음에는 User, Tier, Seat 데이터를 한 번에 요청하여 화면에 표시
    getAllData();
};

// User, Tier, Seat 데이터를 한 번에 가져오는 함수
async function getAllData() {
    try {
        const response = await axios.post('/admin/getAllData', {});

        const { tiers, users, seats } = response.data;

        // User와 Tier 데이터를 테이블에 추가
        updateTableData(users, tiers);

        // Seat 데이터 페이징을 위한 기본 값만 설정
        totalPages = Math.ceil(seats.length / pageSize);  // 전체 페이지 수 계산
        updatePaginationButtons();  // 페이지 버튼 업데이트

        // 페이지에 맞는 Seat 데이터를 가져옵니다.
        fetchSeats(currentPage);
    } catch (error) {
        console.error('데이터 조회 오류:', error);
    }
}

// 페이지 번호에 맞는 Seat 데이터 가져오는 함수
async function fetchSeats(page) {
    try {
        const response = await axios.post('/admin/paged', {
            page: page,      // 페이지 번호
            size: pageSize   // 페이지당 항목 수
        });

        const seats = response.data.content;
        totalPages = response.data.totalPages; // 전체 페이지 수 업데이트

        // Seat 데이터를 테이블에 추가
        updateSeatData(seats);

        // 페이지 버튼 업데이트
        updatePaginationButtons();
    } catch (error) {
        console.error('Seat 데이터 조회 오류:', error);
    }
}

// Seat 데이터를 테이블에 추가하는 함수
function updateSeatData(seats) {
    const seatTableBody = document.querySelector('#seatTable tbody');
    seatTableBody.innerHTML = '';  // 기존 테이블 내용 삭제
    seats.forEach(seat => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${seat.seatId}</td>
            <td>${seat.section}</td>
            <td style="text-align: right;">${seat.price.toLocaleString()} 원</td>
        `;
        seatTableBody.appendChild(row);
    });
}

/// User와 Tier 데이터를 테이블에 추가하는 함수
function updateTableData(users, tiers) {
    // User 테이블 갱신
    const userTableBody = document.querySelector('#userTable tbody');
    userTableBody.innerHTML = '';  // 기존 테이블 내용 삭제

    // 'admin'이 아닌 사용자만 표시
    const filteredUsers = users.filter(user => user.userId !== 'admin');

    filteredUsers.forEach(user => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${user.userId}</td>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.phoneNumber}</td>
            <td>${user.address1} ${user.address2}</td>
            <td>${user.postcode}</td>
            <td>${user.tierName}</td>
            <td>
                <button class="btn btn-danger delete-user-btn" data-user-id="${user.userId}" onclick="deleteUser(this)">삭제</button>
            </td>
        `;
        userTableBody.appendChild(row);
    });

    // Tier 테이블 갱신
    const tierTableBody = document.querySelector('#tierTable tbody');
    tierTableBody.innerHTML = '';  // 기존 테이블 내용 삭제
    tiers.forEach(tier => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${tier.tierId}</td>
            <td>${tier.tierName}</td>
            <td>${tier.minSpent.toLocaleString()} 원</td>
            <td>${tier.maxSpent.toLocaleString()} 원</td>
            <td>${tier.discountRate} %</td>
            <td>
            <button 
                class="btn btn-primary update-tier-btn" 
                data-tier-id="${tier.tierId}" onclick="updateTier(this)">
                수정
            </button>
            </td>
        `;
        tierTableBody.appendChild(row);
    });
}

// 페이징 버튼 업데이트
function updatePaginationButtons() {
    const prevButton = document.getElementById('prevPage');
    const nextButton = document.getElementById('nextPage');
    const pageNumbers = document.getElementById('pageNumbers');

    // 페이지 번호 표시
    pageNumbers.innerHTML = `${currentPage + 1} / ${totalPages}`;

    // 이전, 다음 페이지 버튼 활성화 상태 설정
    prevButton.disabled = currentPage <= 0;
    nextButton.disabled = currentPage >= totalPages - 1;
}

// 페이지 변경 함수
function changePage(direction) {
    currentPage += direction;
    fetchSeats(currentPage);  // Seat 데이터만 새로 요청
    updatePaginationButtons();  // 페이지 버튼 업데이트
}

// 사용자 삭제 처리
async function deleteUser(button) {
    const userId = button.getAttribute('data-user-id');
    try {
        await axios.delete(`/admin/user/${userId}`);
        button.closest('tr').remove();  // 삭제된 사용자 행을 테이블에서 제거
    } catch (error) {
        console.error('사용자 삭제 오류:', error);
    }
}

// 페이지를 변경하는 버튼 클릭 시 호출되는 함수
document.getElementById('prevPage').addEventListener('click', function() {
    changePage(-1);
});

document.getElementById('nextPage').addEventListener('click', function() {
    changePage(1);
});

// 초기 데이터 로드
getAllData();