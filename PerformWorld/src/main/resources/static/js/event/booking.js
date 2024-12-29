const init = () => {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);

    // maxDate를 임시 데이터로 설정
    const maxDate = "2025-12-31";

    // flatpickr 초기화
    const fp = flatpickr("#datepicker", {
        inline: true,           // 달력을 div에 바로 표시
        dateFormat: "Y-m",      // 날짜 형식 (연도-월)
        minDate: tomorrow,      // 오늘 날짜부터 선택 가능
        maxDate: maxDate,       // 최대 날짜
        disableMobile: true,    // 모바일에서의 UI 변경 방지
        onReady: function(selectedDates, dateStr, instance) {
            const year = instance.currentYear;
            const month = instance.currentMonth;
            instance.calendarContainer.querySelector('.flatpickr-monthDropdown-month').textContent = `${year}년 ${month+1}월`;

            // 월 선택 비활성화
            const selectElement = instance.calendarContainer.querySelector('.flatpickr-monthDropdown-months');
            if (selectElement) {
                selectElement.disabled = true;
            }

            // 월 선택 드롭다운 스타일링
            const monthDropdown = instance.calendarContainer.querySelector('.flatpickr-monthDropdown-months');
            if (monthDropdown) {
                monthDropdown.style.webkitAppearance = "none";
                monthDropdown.style.mozAppearance = "none";
                monthDropdown.style.oAppearance = "none";
                monthDropdown.style.appearance = "none";
            }

            // 날짜 입력창 숨기기
            const numInputWrapper = instance.calendarContainer.querySelector('.numInputWrapper');
            if (numInputWrapper) {
                numInputWrapper.style.display = "none";
            }
        },
        onMonthChange: function(selectedDates, dateStr, instance) {
            console.log(instance)
            const year = instance.currentYear;
            const month = instance.currentMonth;
            instance.calendarContainer.querySelector(`.flatpickr-monthDropdown-months option[value="${month}"]`).textContent = `${year}년 ${month+1}월`;
        }
    });

    // 좌석 선택 개수 추적 변수
    let selectedSeats = [];

    // 좌석 데이터 생성 (기존 코드 그대로 사용)
    const seatData = [];
    const sections = ['A', 'B', 'C', 'D', 'E'];
    const prices = {
        VIP: 100000,
        R: 70000,
        S: 50000
    };

    // 각 구역마다 10개의 좌석을 생성
    sections.forEach((section) => {
        let grade = ''; // 좌석 등급
        let seatCount = 10;  // 각 구역에 10개씩 좌석을 생성

        // 구역에 따라 좌석 등급을 결정
        for (let i = 1; i <= seatCount; i++) {
            const seatId = `${section}${i}`;
            let price = prices['R'];  // 기본적으로 R 등급을 설정

            // 1번, 10번 좌석은 S 등급
            if (i === 1 || i === 10) {
                grade = 'S';
                price = prices['S'];
            }
            // E 구역은 전체 S 등급
            else if (section === 'E') {
                grade = 'S';
                price = prices['S'];
            }
            // 3번부터 8번까지는 VIP 등급 (D 구역 제외)
            else if (i >= 3 && i <= 8 && section !== 'D') {
                grade = 'VIP';
                price = prices['VIP'];
            }
            // 그 외 좌석은 R 등급
            else {
                grade = 'R';
                price = prices['R'];
            }

            // 좌석 객체 추가
            seatData.push({
                seatId: seatId,
                section: section,
                price: price,
                grade: grade
            });
        }
    });
    console.log(seatData);

    const seatContainer = document.getElementById("seat-container");

    // 좌석 생성 및 클릭 이벤트 처리
    seatData.forEach(seat => {
        const seatElement = document.createElement("div");
        seatElement.classList.add("seat");
        seatElement.setAttribute("data-seat-id", seat.seatId);
        seatElement.setAttribute("data-grade", seat.grade);
        seatElement.setAttribute("data-price", seat.price);
        seatElement.textContent = seat.seatId; // 좌석 ID 표시

        // 좌석 클릭 시 선택 상태 변경
        seatElement.addEventListener("click", function() {
            // 이미 선택된 좌석인 경우
            if (this.classList.contains("selected")) {
                this.classList.remove("selected");
                selectedSeats = selectedSeats.filter(seat => seat !== this.getAttribute("data-seat-id"));
            }
            // 선택된 좌석이 2개 미만일 때만 선택
            else if (selectedSeats.length < 2) {
                this.classList.add("selected");
                selectedSeats.push(this.getAttribute("data-seat-id"));
            } else {
                alert("선택 가능한 좌석 수를 초과했습니다.");
            }

            console.log(selectedSeats);
        });

        // 좌석 구역별 색상 구분
        switch (seat.grade) {
            case 'VIP':
                seatElement.classList.add("vip-seat");
                break;
            case 'R':
                seatElement.classList.add("r-seat");
                break;
            case 'S':
                seatElement.classList.add("s-seat");
                break;
        }

        seatContainer.appendChild(seatElement);
    });
}

window.onload = () => {
    init();
}
