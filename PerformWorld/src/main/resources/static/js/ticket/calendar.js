document.addEventListener("DOMContentLoaded", function () {
    let bookedDates = []; // 예매된 날짜 데이터 저장 변수

    // 예매된 날짜를 서버에서 가져오기
    function fetchBookedDates() {
        const status = 'Y'; // 예매 완료 상태만 조회
        axios.get(`/ticket/calendar/api/booked-dates?status=${status}`)
            .then(response => {
                bookedDates = response.data || []; // 응답 데이터 저장
                initializeCalendar(); // 캘린더 초기화
            })
            .catch(error => {
                console.error('예매된 날짜를 가져오는 데 실패했습니다:', error);
                alert('예매된 날짜를 가져오는 데 실패했습니다. 서버 오류가 발생했습니다.');
            });
    }

    // Toast UI Calendar 초기화
    function initializeCalendar() {
        const calendar = new tui.Calendar('#calendar', {
            defaultView: 'month', // 기본 월별 보기
            taskView: ['milestone', 'task'],
            scheduleView: ['time', 'allday'],
            // useDetailPopup: true,  // 상세보기 팝업 사용
            selectable: false, // 날짜 선택 비활성화
            isReadOnly: true,
            template: {
                milestone: schedule => `<span style="color:${schedule.color};">${schedule.title}</span>`,
            },
            locale: 'ko', // 한글 표시
        });

        // 예매된 날짜를 캘린더에 추가
        const events = bookedDates.map(event => {
            if (event && event.dateTime && event.title) {
                const [date, time] = event.dateTime.split(' ');
                const [year, month, day] = date.split('-');
                const [hour, minute, second] = time.split(':');
                const startDate = new Date(year, month - 1, day, hour, minute, second);
                const endDate = new Date(startDate);

                return {
                    id: event.dateTime,
                    calendarId: 'cal1',
                    title: event.title,
                    start: startDate.toISOString(),
                    end: endDate.toISOString(),
                    isAllday: false,
                    category: 'time',
                    backgroundColor: '#ff6f61',
                    borderColor: '#ff6f61'
                };
            }
            return null;
        }).filter(event => event !== null);

        calendar.createEvents(events);

        // 초기 년/월 표시 업데이트
        updateCalendarTitle(calendar);

        // 버튼 이벤트 초기화
        initializeMenu(calendar);

        // 일정 클릭 시 팝업 위치 조정
        // calendar.on('beforeOpenDetailPopup', (event) => {
        //     adjustPopupPosition(event);
        // });
    }

    // 년/월 표시를 업데이트하는 함수
    function updateCalendarTitle(calendar) {
        const currentDate = calendar.getDate(); // 현재 표시 중인 날짜
        const year = currentDate.getFullYear(); // 현재 년도
        const month = currentDate.getMonth() + 1; // 현재 월
        const calendarTitle = document.getElementById('calendar-title'); // 제목 영역
        if (calendarTitle) {
            calendarTitle.textContent = `${year}년 ${month}월`;
        }
    }

    // 메뉴 버튼 이벤트 초기화
    function initializeMenu(calendar) {
        document.getElementById('btn-month-view').addEventListener('click', () => {
            calendar.changeView('month', true); // 월별 보기로 전환
            updateCalendarTitle(calendar);
        });

        document.getElementById('btn-week-view').addEventListener('click', () => {
            calendar.changeView('week', true); // 주별 보기로 전환
            updateCalendarTitle(calendar);
        });

        document.getElementById('btn-today').addEventListener('click', () => {
            calendar.today(); // 오늘 날짜로 이동
            updateCalendarTitle(calendar);
        });

        // 이전/다음 버튼 클릭 시 월 이동
        document.getElementById('btn-prev-month').addEventListener('click', () => {
            calendar.prev(); // 이전 월로 이동
            updateCalendarTitle(calendar);
        });

        document.getElementById('btn-next-month').addEventListener('click', () => {
            calendar.next(); // 다음 월로 이동
            updateCalendarTitle(calendar);
        });

        // 캘린더에서 일정 클릭 시 제목 업데이트
        // calendar.on('beforeRenderSchedule', () => updateCalendarTitle(calendar));
    }

    // function adjustPopupPosition(event) {
    //     const popup = document.querySelector('.tui-full-calendar-popup'); // 팝업 요소
    //     const scheduleElement = event.schedule; // 클릭된 일정 요소
    //     if (popup && scheduleElement) {
    //         const scheduleRect = scheduleElement.getBoundingClientRect(); // 일정 요소의 위치
    //         const popupRect = popup.getBoundingClientRect(); // 팝업 요소의 위치
    //
    //         // X 축: 일정의 왼쪽을 기준으로 팝업을 위치시킴
    //         const offsetX = scheduleRect.left;
    //
    //         // Y 축: 일정 위로 팝업을 표시 (기존 방식 유지)
    //         const offsetY = scheduleRect.top - popupRect.height - 10;
    //
    //         // 팝업 위치를 클릭된 일정 위치로 설정
    //         popup.style.left = `${offsetX}px`;
    //         popup.style.top = `${offsetY}px`;
    //     }
    // }

    // 데이터 가져오기 시작
    fetchBookedDates();
});