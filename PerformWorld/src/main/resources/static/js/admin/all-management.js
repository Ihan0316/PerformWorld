// 비동기 함수로 전체 데이터를 한 번에 조회
async function getAllData() {
    try {
        // 로딩 상태 표시
        document.getElementById('loadingSpinner').style.display = 'block';

        // 전체 데이터를 한번에 받아오는 요청
        const response = await axios.post('/admin/getAllData', {
            // 요청 데이터 (예: 필터링을 위한 파라미터)
            tierFilter: ''  // 예시: 필터링할 값
        });

        // 받은 데이터를 변수에 저장
        const userList = response.data.users;
        const tierList = response.data.tiers;
        const seatList = response.data.seats;

        // 결과 출력 (예시)
        console.log('User List:', userList);
        console.log('Tier List:', tierList);
        console.log('Seat List:', seatList);

        // 받은 데이터를 화면에 갱신하는 함수 호출
        updateTableData(userList, tierList, seatList);
    } catch (error) {
        console.error('데이터 조회 오류:', error);
    } finally {
        // 로딩 상태 숨기기
        document.getElementById('loadingSpinner').style.display = 'none';
    }
}

// 데이터를 화면에 표시하는 함수
function updateTableData(users, tiers, seats) {
    // User 테이블 갱신
    const userTableBody = document.querySelector('#userTable tbody');
    userTableBody.innerHTML = ''; // 기존 테이블 내용 삭제
    users.forEach(user => {
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
    tierTableBody.innerHTML = ''; // 기존 테이블 내용 삭제
    tiers.forEach(tier => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${tier.tierId}</td>
            <td>${tier.tierName}</td>
            <td>${tier.minSpent.toLocaleString()} 원</td>
            <td>${tier.maxSpent.toLocaleString()} 원</td>
            <td>${tier.discountRate} %</td>
        `;
        tierTableBody.appendChild(row);
    });

    // Seat 테이블 갱신
    const seatTableBody = document.querySelector('#seatTable tbody');
    seatTableBody.innerHTML = ''; // 기존 테이블 내용 삭제
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

// 페이지가 로드되면 데이터를 조회
window.onload = getAllData;