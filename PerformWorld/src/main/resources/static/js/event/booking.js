const init = () => {

    let userInfo = {};  // 회원 정보
    let eventName = "";  // 공연명
    let sections = [];  // 좌석 등급 정보
    let discountPercent = 0;  // 등급 할인율
    let selectedSeats = [];  // 좌석 선택 개수
    let selectedSchedule = null;  // 선택된 회차 id
    const totalPay = document.querySelector(".totalAmount");

    getUserInfo().then(res => {
        userInfo = res.data;
    }).catch(e => {
        alert("회원 정보를 가져오는데 실패했습니다.");
    });

    getUserTier().then(res => {
        discountPercent = (100-res.discountRate)/100;
        document.querySelector(".discountAmount").textContent = res.discountRate;
    }).catch(e => {
        alert("등급 정보를 가져오는데 실패했습니다.");
    });

    // 좌석선택
    getSeatList().then(res => {
        let seatPrice = 0;

        res.forEach(seat => {
            const seatElement = document.createElement("div");
            seatElement.classList.add("seat");
            seatElement.setAttribute("data-seat-id", seat.seatId);
            seatElement.setAttribute("data-grade", seat.section);
            seatElement.setAttribute("data-price", seat.price);
            seatElement.textContent = seat.seatId; // 좌석 ID 표시

            // 좌석 클릭 시 선택 상태 변경
            seatElement.addEventListener("click", function() {

                // 이미 선택된 좌석인 경우
                if (this.classList.contains("selected")) {
                    this.classList.remove("selected");
                    selectedSeats = selectedSeats.filter(seat => seat !== this.getAttribute("data-seat-id"));
                    // 결제금액 세팅
                    seatPrice -= seat.price;
                    if(document.querySelector("input[name='isDelivery']").checked) {
                        totalPay.textContent = (seatPrice * discountPercent + 3000).toLocaleString();
                    } else {
                        totalPay.textContent = (seatPrice * discountPercent).toLocaleString();
                    }
                }

                // 선택된 좌석이 2개 미만일 때만 선택
                else if (selectedSeats.length < 2) {
                    this.classList.add("selected");
                    selectedSeats.push(this.getAttribute("data-seat-id"));
                    // 결제금액 세팅
                    seatPrice += seat.price;
                    if(document.querySelector("input[name='isDelivery']").checked) {
                        totalPay.textContent = (seatPrice * discountPercent + 3000).toLocaleString();
                    } else {
                        totalPay.textContent = (seatPrice * discountPercent).toLocaleString();
                    }
                } else {
                    alert("선택 가능한 좌석 수를 초과했습니다.");
                    return;
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
            });

            // sections 세팅
            const sectionExists = sections.some(item => item.section === seat.section);
            if (!sectionExists) {
                sections.push({
                    section: seat.section,
                    price: seat.price,
                    color: seat.section === 'VIP' ? 'mediumorchid' : seat.section === 'R' ? 'mediumseagreen' : 'skyblue'
                });
            }

            // 좌석 구역별 색상 구분
            seatElement.classList.add(`${seat.section}-seat`);

            document.getElementById("seat-container").appendChild(seatElement);
        });
        sections.sort((a, b) => b.price - a.price);

        // 좌석 정보 추가
        const sectionInfoContainer = document.querySelector(".section-info");
        sections.forEach(section => {
            const sectionElement = document.createElement("div");
            sectionElement.classList.add("section-info-item");

            sectionElement.innerHTML = `
                <div class="section-name" style="color: ${section.color}">${section.section}</div>
                <div class="section-price">${section.price.toLocaleString()}원</div>
            `;

            sectionInfoContainer.appendChild(sectionElement);
        });

    }).catch(e => {
        alert("좌석 정보를 가져오는데 실패했습니다.");
    });

    getTicketingInfo().then(res => {
        console.log(res);
        const datePeriod = getMixMaxDate(res);

        eventName = res[0].eventName;
        document.querySelector(".eventName").textContent = eventName;

        // 날짜선택 (flatpickr)
        const fp = flatpickr("#datepicker", {
            inline: true,               // 달력을 div에 바로 표시
            dateFormat: "Y-m-d",        // 날짜 형식 (연도-월-일)
            minDate: datePeriod.start,  // 티켓팅 시작일
            maxDate: datePeriod.end,    // 티켓팅 종료일
            disableMobile: true,        // 모바일에서의 UI 변경 방지
            onReady: function(selectedDates, dateStr, instance) {
                // 초기 세팅
                const year = instance.currentYear;
                const month = instance.currentMonth;
                instance.calendarContainer.querySelector('.flatpickr-monthDropdown-month').textContent = `${year}년 ${month + 1}월`;

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
                // 월 변경 event
                const year = instance.currentYear;
                const month = instance.currentMonth;
                instance.calendarContainer.querySelector(`.flatpickr-monthDropdown-months option[value="${month}"]`).textContent = `${year}년 ${month + 1}월`;
            },
            onChange: function(selectedDates, dateStr, instance) {
                // 날짜 선택 event
                getScheduleList(new Date(selectedDates[0]).toLocaleDateString('sv-SE', {timeZone: 'Asia/Seoul'})).then(res => {
                    console.log(res);

                    // 회차 정보 표시
                    const scheduleInfoDiv = document.querySelector(".scheduleInfo");
                    scheduleInfoDiv.innerHTML = ''; // 이전 정보 지우기

                    if (res.length > 0) {
                        res.forEach(schedule => {
                            const scheduleElement = document.createElement("div");
                            scheduleElement.classList.add("schedule-item");
                            scheduleElement.innerHTML = `
                                <div class="d-flex justify-content-between">
                                    <strong>${schedule.eventDate.split('T')[1].substring(0, 5)}</strong>
                                    <span>${schedule.seats.length}/50</span>
                                </div>
                                <div><span>${schedule.eventCast}</span></div>
                            `;

                            // 클릭 이벤트 핸들러
                            scheduleElement.addEventListener("click", function() {
                                selectedSchedule = schedule.scheduleId;

                                // 이전에 클릭한 항목 active 클래스 제거
                                const activeItem = document.querySelector(".schedule-item.active");
                                if (activeItem) {
                                    activeItem.classList.remove("active");
                                }
                                // 클릭된 항목 active 클래스 추가
                                scheduleElement.classList.add("active");

                                // 좌석 초기화
                                selectedSeats = [];
                                const selectedSeatElements = document.querySelectorAll(".seat.selected");
                                selectedSeatElements.forEach(seat => {
                                    seat.classList.remove("selected");
                                });
                                const bookedSeatElements = document.querySelectorAll(".seat.unavailable");
                                bookedSeatElements.forEach(seat => {
                                    seat.classList.remove("unavailable");
                                });
                                // 예매된 좌석 선택불가 설정
                                schedule.seats.forEach(seat => {
                                    const bookedSeat = document.querySelector(`.seat[data-seat-id='${seat}']`);
                                    if (bookedSeat) {
                                        bookedSeat.classList.add("unavailable");
                                    }
                                });

                                // 좌석 선택 UI 보이기
                                document.querySelector(".seatSelBox").classList.remove("d-none");
                                // 배송 여부와 결제하기 UI 숨기기
                                document.querySelector(".dlvSelBox").classList.add("d-none");
                                document.querySelector(".booking-footer").classList.add("d-none");
                            });

                            scheduleInfoDiv.appendChild(scheduleElement);
                        });
                    } else {
                        scheduleInfoDiv.innerHTML = '<div>선택한 날짜에 공연 정보가 없습니다.</div>';
                    }

                }).catch(e => {
                    alert("회차 정보를 가져오는데 실패했습니다.");
                });

                // 날짜 변경 시 좌석 선택창 숨기기
                document.querySelector(".seatSelBox").classList.add("d-none");
                // 배송 여부와 결제하기 UI 숨기기
                document.querySelector(".dlvSelBox").classList.add("d-none");
                document.querySelector(".booking-footer").classList.add("d-none");
            }
        });
    }).catch(e => {
        // 티켓팅 가능 회차 없으면 공연 상세페이지로
        alert("티켓팅 정보를 불러오는데 실패했습니다.");
        window.location.href = `/event/details/${eventId}`;
    });

    // 모든 티켓팅의 오픈 회차(날짜) 범위 합치기
    function getMixMaxDate(periods) {
        let minDate = periods[0].eventPeriodStart;
        let maxDate = periods[0].eventPeriodEnd;

        periods.forEach(period => {
            if (new Date(period.eventPeriodStart) < new Date(minDate)) {
                minDate = period.eventPeriodStart; // 더 이른 날짜로 갱신
            }
            if (new Date(period.eventPeriodEnd) > new Date(maxDate)) {
                maxDate = period.eventPeriodEnd; // 더 늦은 날짜로 갱신
            }
        });
        // minDate는 내일 날짜보다 이전일 수 없음
        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        if (tomorrow > new Date(minDate)) {
            minDate = tomorrow;
        }

        return { start: minDate, end: maxDate };
    }

    // 배송 받기 mode 변경
    document.querySelector("input[name='isDelivery']").addEventListener("change", function(e) {
        if (this.checked) {
            document.querySelector(".shippingAmount").textContent = '3,000';
            totalPay.textContent = (Number(totalPay.textContent.replace(/,/g, '')) + 3000).toLocaleString();
            document.querySelector("input[name='address2']").disabled = false;
            document.querySelector("input[name='loadAddress']").disabled = false;
            document.querySelector("input[name='openPostBtn']").disabled = false;
        } else {
            document.querySelector(".shippingAmount").textContent = '0';
            totalPay.textContent = (Number(totalPay.textContent.replace(/,/g, '')) - 3000).toLocaleString();
            document.querySelector("input[name='postcode']").value = '';
            document.querySelector("input[name='address1']").value = '';
            document.querySelector("input[name='address2']").value = '';
            document.querySelector("input[name='address2']").disabled = true;
            document.querySelector("input[name='loadAddress']").disabled = true;
            document.querySelector("input[name='loadAddress']").checked = false;
            document.querySelector("input[name='openPostBtn']").disabled = true;
        }
    });

    // 배송지 불러오기
    document.querySelector("input[name='loadAddress']").addEventListener("click", function(e) {
        document.querySelector("input[name='postcode']").value = userInfo.postcode;
        document.querySelector("input[name='address1']").value = userInfo.address1;
        document.querySelector("input[name='address2']").value = userInfo.address2;
    });

    // 배송지 입력
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



    // 회원 정보 가져오기
    async function getUserInfo() {
        const res = await axios({
            method: 'post',
            url: '/user/getInfo',
            data: { userId: 'user123' }, // loginInfo
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return res;
    }

    // 등급 정보 가져오기
    async function getUserTier() {
        const res = await axios({
            method : 'post',
            url : '/book/getUserTier',
            data : { userId: 'user123' },  // loginInfo
            headers : {
                'Content-Type' : 'application/json'
            }
        });
        return res.data;
    }

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

    // 회차 목록 가져오기
    async function getScheduleList(searchDate) {
        const res = await axios({
            method : 'post',
            url : '/eventSchedule/getScheduleList',
            data : {
                eventId: eventId,
                searchDate: searchDate
            },
            headers : {
                'Content-Type' : 'application/json'
            }
        });
        return res.data;
    }

    // 좌석 정보 가져오기
    async function getSeatList() {
        const res = await axios({
            method : 'post',
            url : '/book/getSeatList',
            headers : {
                'Content-Type' : 'application/json'
            }
        });
        return res.data;
    }

    // 결제하기
    document.querySelector(".payBtn").addEventListener("click", async function(e) {
        const postcode = document.querySelector("input[name='postcode']").value;
        if(document.querySelector("input[name='isDelivery']").checked &&
            (postcode == null || postcode === '')) {
            alert("배송지를 선택해주세요.");
            return;
        }

        // 결제 api
        const result = await PortOne.requestPayment({
            storeId: "store-e12bb050-7a06-49db-a3d6-22120ea4d145",
            channelKey: "channel-key-2cd8fabb-10e2-4788-bd23-93fe53a7cba2",
            paymentId: `pay${crypto.randomUUID()}`,
            orderName: eventName,
            totalAmount: totalPay.textContent.replace(/,/g, ''),
            currency: "CURRENCY_KRW",
            payMethod: "CARD",
            customer: {
              fullName: userInfo.name,
              phoneNumber: userInfo.phoneNumber,
              email: userInfo.email,
            },
        });
        if (result.code !== undefined) {
            return alert(result.message);
        }

        // 결제정보 DB 저장
        let address = "";
        if(document.querySelector("input[name='isDelivery']").checked) {
            address = `${document.querySelector("input[name='address1']").value} ${document.querySelector("input[name='address2']").value} (${document.querySelector("input[name='postcode']").value})`;
        }
        await axios({
            method : 'post',
            url : '/pay/regist',
            data : {
                userId: 'user123',  // loginInfo
                scheduleId: selectedSchedule,
                seatIds: selectedSeats,
                isDelivery: document.querySelector("input[name='isDelivery']").checked,
                address: address,
                totalPrice: totalPay.textContent.replace(/,/g, ''),
                payment: { paymentId: result.paymentId }
            },
            headers : {
                'Content-Type' : 'application/json'
            }
        }).then(res => {
            switch (res.data.resultCode) {
                case 200 :
                    alert("예매가 완료되었습니다.");
                    console.log(res.data.bookingId)
                    // window.location.href = `/user/book/${res.data.bookingId}`;
                    break;
                case 900 :
                    alert("이미 결제된 좌석입니다.");
                    break;
                case 800 :
                    alert("결제 정보 조회에 실패했습니다.");
                    break;
                default : alert("결제에 실패했습니다.");
            }

        }).catch(e => {
            alert("결제에 실패했습니다.");
        });
    });
}

window.onload = () => {
    init();
}
