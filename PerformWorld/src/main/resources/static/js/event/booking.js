const init = () => {

//     // 여러 티켓팅의 날짜 범위 예시
//     const ticketingPeriods = [
//         { start: "2024-12-01", end: "2024-12-05" }, // 1차 티켓팅 범위
//         { start: "2024-12-03", end: "2024-12-07" }, // 2차 티켓팅 범위
//         { start: "2024-12-06", end: "2024-12-10" }  // 3차 티켓팅 범위
//     ];
//
// // 날짜 범위의 합집합을 계산하는 함수
//     function getUnionPeriod(periods) {
//         let minDate = periods[0].start;
//         let maxDate = periods[0].end;
//
//         // 모든 티켓팅 범위를 합치기
//         periods.forEach(period => {
//             if (new Date(period.start) < new Date(minDate)) {
//                 minDate = period.start; // 더 이른 날짜로 갱신
//             }
//             if (new Date(period.end) > new Date(maxDate)) {
//                 maxDate = period.end; // 더 늦은 날짜로 갱신
//             }
//         });
//
//         return { start: minDate, end: maxDate };
//     }
//
// // 합집합 범위 계산
//     const unionPeriod = getUnionPeriod(ticketingPeriods);
//     console.log(unionPeriod);  // { start: '2024-12-01', end: '2024-12-10' }
//
// // flatpickr로 날짜 범위 설정
//     getTicketingInfo().then(res => {
//         console.log(res);
//         const eventPeriodStart = unionPeriod.start;  // 합집합 시작일
//         const eventPeriodEnd = unionPeriod.end;      // 합집합 종료일
//
//         // flatpickr 초기화
//         const fp = flatpickr("#datepicker", {
//             inline: true,           // 달력을 div에 바로 표시
//             dateFormat: "Y-m-d",    // 날짜 형식 (연도-월-일)
//             minDate: eventPeriodStart, // 티켓팅 시작일을 최소 날짜로 설정
//             maxDate: eventPeriodEnd,  // 티켓팅 종료일을 최대 날짜로 설정
//             disableMobile: true,    // 모바일에서의 UI 변경 방지
//             onReady: function(selectedDates, dateStr, instance) {
//                 const year = instance.currentYear;
//                 const month = instance.currentMonth;
//                 instance.calendarContainer.querySelector('.flatpickr-monthDropdown-month').textContent = `${year}년 ${month + 1}월`;
//
//                 // 월 선택 비활성화
//                 const selectElement = instance.calendarContainer.querySelector('.flatpickr-monthDropdown-months');
//                 if (selectElement) {
//                     selectElement.disabled = true;
//                 }
//
//                 // 월 선택 드롭다운 스타일링
//                 const monthDropdown = instance.calendarContainer.querySelector('.flatpickr-monthDropdown-months');
//                 if (monthDropdown) {
//                     monthDropdown.style.webkitAppearance = "none";
//                     monthDropdown.style.mozAppearance = "none";
//                     monthDropdown.style.oAppearance = "none";
//                     monthDropdown.style.appearance = "none";
//                 }
//
//                 // 날짜 입력창 숨기기
//                 const numInputWrapper = instance.calendarContainer.querySelector('.numInputWrapper');
//                 if (numInputWrapper) {
//                     numInputWrapper.style.display = "none";
//                 }
//             },
//             onMonthChange: function(selectedDates, dateStr, instance) {
//                 const year = instance.currentYear;
//                 const month = instance.currentMonth;
//                 instance.calendarContainer.querySelector(`.flatpickr-monthDropdown-months option[value="${month}"]`).textContent = `${year}년 ${month + 1}월`;
//             },
//             onChange: function(selectedDates, dateStr, instance) {
//                 // 선택된 날짜를 기준으로 회차 정보 필터링
//                 const selectedDate = new Date(selectedDates[0]).toLocaleDateString();
//
//                 // 선택된 날짜에 맞는 공연 회차 정보 필터링
//                 const selectedSchedules = eventSchedules.filter(schedule => {
//                     const eventDate = new Date(schedule.eventDate).toLocaleDateString();
//                     return eventDate === selectedDate; // 날짜만 비교
//                 });
//
//                 // 회차 정보 표시
//                 const scheduleInfoDiv = document.querySelector(".scheduleInfo");
//                 scheduleInfoDiv.innerHTML = ''; // 이전 정보 지우기
//
//                 if (selectedSchedules.length > 0) {
//                     selectedSchedules.forEach(schedule => {
//                         const scheduleElement = document.createElement("div");
//                         scheduleElement.classList.add("schedule-item");
//                         scheduleElement.innerHTML = `
//                         <div><strong>${schedule.eventDate.split('T')[1].substring(0, 5)}</strong></div>
//                         <div><span>${schedule.eventCast}</span></div>
//                     `;
//
//                         // 클릭 가능한 이벤트 핸들러 추가
//                         scheduleElement.addEventListener("click", function() {
//                             // 이전에 클릭한 항목이 있다면 active 클래스를 제거
//                             const activeItem = document.querySelector(".schedule-item.active");
//                             if (activeItem) {
//                                 activeItem.classList.remove("active");
//                             }
//
//                             // 클릭된 항목에 active 클래스 추가
//                             scheduleElement.classList.add("active");
//
//                             // 회차 선택이 완료되면 좌석 선택 UI 보이기
//                             document.querySelector(".seatSelBox").classList.remove("d-none");
//
//                             // 좌석 초기화
//                             selectedSeats = [];
//                             // 선택된 좌석 UI 초기화
//                             const selectedSeatElements = document.querySelectorAll(".seat.selected");
//                             selectedSeatElements.forEach(seat => {
//                                 seat.classList.remove("selected");
//                             });
//                             // 배송 여부와 결제하기 UI 숨기기
//                             document.querySelector(".dlvSelBox").classList.add("d-none");
//                             document.querySelector(".booking-footer").classList.add("d-none");
//                         });
//
//                         scheduleInfoDiv.appendChild(scheduleElement);
//                     });
//                 } else {
//                     scheduleInfoDiv.innerHTML = '<div>선택한 날짜에 공연 정보가 없습니다.</div>';
//                 }
//
//                 // 날짜 변경 시 좌석 선택창 숨기기
//                 document.querySelector(".seatSelBox").classList.add("d-none");
//                 // 배송 여부와 결제하기 UI 숨기기
//                 document.querySelector(".dlvSelBox").classList.add("d-none");
//                 document.querySelector(".booking-footer").classList.add("d-none");
//             }
//         });
//     }).catch(e => {
//         alert("티켓팅 정보를 불러오는데 실패했습니다.");
//     });


    // 티켓팅 목록 가져오기
    async function getTicketingInfo() {
        const res = await axios({
            method : 'post',
            url : '/book/getEventTicketing',
            data : { eventId: eventId },
            headers : {
                'Content-Type' : 'application/json'
            }
        });
        return res.data;
    }

    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);

    // maxDate를 임시 데이터로 설정
    const maxDate = new Date("2025-12-31");

    const eventSchedules = [
        {
            eventDate: "2025-01-01T14:00:00",
            eventCast: "출연진, 출연진, 출연진, 출연진, 출연진, 출연진, 출연진",
        },
        {
            eventDate: "2025-01-01T19:00:00",
            eventCast: "출연진 A",
        },
        {
            eventDate: "2025-01-02T19:30:00",
            eventCast: "출연진 B, 출연진 C",
        }
    ];

    // flatpickr 초기화
    const fp = flatpickr("#datepicker", {
        inline: true,           // 달력을 div에 바로 표시
        dateFormat: "Y-m-d",    // 날짜 형식 (연도-월-일)
        minDate: tomorrow,      // 내일 날짜부터 선택 가능
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
        },
        onChange: function(selectedDates, dateStr, instance) {
            // 선택된 날짜를 기준으로 회차 정보 필터링
            const selectedDate = new Date(selectedDates[0]).toLocaleDateString();

            // 선택된 날짜에 맞는 공연 회차 정보 필터링
            const selectedSchedules = eventSchedules.filter(schedule => {
                const eventDate = new Date(schedule.eventDate).toLocaleDateString();
                return eventDate === selectedDate; // 날짜만 비교
            });

            // 회차 정보 표시
            const scheduleInfoDiv = document.querySelector(".scheduleInfo");
            scheduleInfoDiv.innerHTML = ''; // 이전 정보 지우기

            if (selectedSchedules.length > 0) {
                selectedSchedules.forEach(schedule => {
                    const scheduleElement = document.createElement("div");
                    scheduleElement.classList.add("schedule-item");
                    scheduleElement.innerHTML = `
                <div><strong>${schedule.eventDate.split('T')[1].substring(0, 5)}</strong></div>
                <div><span>${schedule.eventCast}</span></div>
            `;

                    // 클릭 가능한 이벤트 핸들러 추가
                    scheduleElement.addEventListener("click", function() {
                        // 이전에 클릭한 항목이 있다면 active 클래스를 제거
                        const activeItem = document.querySelector(".schedule-item.active");
                        if (activeItem) {
                            activeItem.classList.remove("active");
                        }

                        // 클릭된 항목에 active 클래스 추가
                        scheduleElement.classList.add("active");

                        // 회차 선택이 완료되면 좌석 선택 UI 보이기
                        document.querySelector(".seatSelBox").classList.remove("d-none");

                        // 좌석 초기화
                        selectedSeats = [];
                        // 선택된 좌석 UI 초기화
                        const selectedSeatElements = document.querySelectorAll(".seat.selected");
                        selectedSeatElements.forEach(seat => {
                            seat.classList.remove("selected");
                        });
                        // 배송 여부와 결제하기 UI 숨기기
                        document.querySelector(".dlvSelBox").classList.add("d-none");
                        document.querySelector(".booking-footer").classList.add("d-none");
                    });

                    scheduleInfoDiv.appendChild(scheduleElement);
                });
            } else {
                scheduleInfoDiv.innerHTML = '<div>선택한 날짜에 공연 정보가 없습니다.</div>';
            }

            // 날짜 변경 시 좌석 선택창 숨기기
            document.querySelector(".seatSelBox").classList.add("d-none");
            // 배송 여부와 결제하기 UI 숨기기
            document.querySelector(".dlvSelBox").classList.add("d-none");
            document.querySelector(".booking-footer").classList.add("d-none");
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

            if (selectedSeats.length > 0) {
                // 좌석 선택이 완료되면 배송 여부와 결제 UI 보이기
                document.querySelector(".dlvSelBox").classList.remove("d-none");
                document.querySelector(".booking-footer").classList.remove("d-none");
            } else {
                // 좌석이 선택되지 않으면 배송 여부와 결제 UI 숨기기
                document.querySelector(".dlvSelBox").classList.add("d-none");
                document.querySelector(".booking-footer").classList.add("d-none");
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

    // 배송 받기 mode 변경
    document.querySelector("input[name='isDelivery']").addEventListener("change", function(e) {
        if (this.checked) {
            document.querySelector("input[name='address2']").disabled = false;
            document.querySelector("input[name='openPostBtn']").disabled = false;
        } else {
            document.querySelector("input[name='postcode']").value = '';
            document.querySelector("input[name='address1']").value = '';
            document.querySelector("input[name='address2']").value = '';
            document.querySelector("input[name='address2']").disabled = true;
            document.querySelector("input[name='openPostBtn']").disabled = true;
        }
    });

    // 배송지 선택
    document.querySelector("input[name='openPostBtn']").addEventListener("click", function(e) {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                if(data.userSelectedType === 'R'){
                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가한다.
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    if(extraAddr !== ''){
                        extraAddr = ' (' + extraAddr + ')';
                    }
                    // 조합된 참고항목을 해당 필드에 넣는다.
                    //document.getElementById("sample6_extraAddress").value = extraAddr;

                } else {
                    //document.getElementById("sample6_extraAddress").value = '';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.querySelector("input[name='postcode']").value = data.zonecode;
                document.querySelector("input[name='address1']").value = addr;
                // 커서를 상세주소 필드로 이동한다.
                document.querySelector("input[name='address2']").focus();
            }
        }).open();
    });

    // 결제하기
    document.querySelector(".payBtn").addEventListener("click", function(e) {
        const postcode = document.querySelector("input[name='postcode']").value;
        if(document.querySelector("input[name='isDelivery']").checked &&
            (postcode == null || postcode === '')) {
            alert("배송지를 선택해주세요.");
            return;
        }
    });
}

window.onload = () => {
    init();
}
