document.addEventListener("DOMContentLoaded", function () {
    // 예매된 날짜 데이터를 서버에서 받아오기
    fetchBookedDates();

    // 예매된 날짜를 서버에서 가져오는 함수
    let bookedDates = [];
    function fetchBookedDates() {
        const status = 'Y'; // 예매 완료된 날짜만 가져오기

        axios.get(`/ticket/calendar/api/booked-dates?status=${status}`)
            .then(response => {
                console.log("response:", response);  // 응답 확인
                bookedDates = response.data;  // 응답에서 bookedDates 가져오기
                console.log("bookedDates:", bookedDates);  // 데이터 확인

                // 캘린더 초기화
                initializeCalendar();
            })
            .catch(error => {
                console.error('예매된 날짜를 가져오는 데 실패했습니다:', error);
                alert('예매된 날짜를 가져오는 데 실패했습니다. 서버 오류가 발생했습니다.');
            });
    }

    // Toast UI Calendar 초기화
    function initializeCalendar() {
        const calendar = new tui.Calendar('#calendar', {
            defaultView: 'month',  // 월별 보기로 설정
            taskView: ['milestone', 'task'],
            scheduleView: ['time', 'allday'],
            template: {
                milestone: function(schedule) {
                    return `<span style="color:${schedule.color};">${schedule.title}</span>`;
                }
            },
            useFormPopup: true,      // 일정 생성 팝업 사용
            useDetailPopup: true,    // 일정 상세 팝업 사용
        });

        // 예매된 날짜를 캘린더에 표시
        const events = bookedDates.map(event => {
            if (event && event.dateTime && event.title) {  // event.dateTime과 event.title이 모두 존재하는지 확인
                console.log("Processing event:", event); // 이벤트 정보 확인

                // dateTime을 사용하여 처리
                const dateTimeStr = event.dateTime;  // '2025-01-10 20:00:00.000000' 형식
                const [date, time] = dateTimeStr.split(' ');  // 날짜와 시간을 분리
                const [year, month, day] = date.split('-');
                const [hour, minute, second] = time.split(':');

                // 시작 시간 (2025-01-10 20:00:00 형식)
                const startDate = new Date(year, month - 1, day, hour, minute, second);
                const endDate = new Date(startDate);

                // 일정 객체 생성
                return {
                    id: event.dateTime,
                    calendarId: 'cal1',
                    title: event.title,  // 예매된 제목 사용
                    start: startDate.toISOString(),  // ISO 형식으로 변환
                    end: endDate.toISOString(),  // ISO 형식으로 변환
                    isAllday: false,  // 시간도 포함되어 있으므로 AllDay는 false로 설정
                    category: 'time',
                    backgroundColor: '#ff6f61',  // 예매된 날짜 강조 색상
                    borderColor: '#ff6f61'
                };
            } else {
                console.error('Invalid event or date value:', event); // event 객체가 없거나 날짜 값이 없을 경우
                return null;
            }
        }).filter(event => event !== null); // null인 이벤트 필터링

        // createEvents 메서드를 사용하여 일정을 추가
        calendar.createEvents(events);
    }
});