document.addEventListener("DOMContentLoaded", function() {
    const modal = new bootstrap.Modal(document.querySelector(".eventRegisterModal"));
    const searchEventBtn = document.querySelector(".searchEventBtn");
    const eventListContainer = document.getElementById("eventList");

    searchEventBtn.addEventListener("click", function(e) {
        e.preventDefault();
        e.stopPropagation();

        // 입력값 가져오기
        const performName = document.querySelector("input[name='perform-name']").value;
        const startDate = document.querySelector("input[name='start-date']").value;
        const endDate = document.querySelector("input[name='end-date']").value;
        const locationCode = document.querySelector("select[name='location-code']").value;

        // 공연 시작일과 종료일을 입력하지 않으면 경고 메시지 표시
        if (!startDate || !endDate) {
            alert("공연 시작일과 종료일을 선택해 주세요.");
            return;  // 함수 종료하여 API 호출을 막음
        }
        // 종료일이 시작일보다 이전이면 경고 메시지 표시
        const start = new Date(startDate);
        const end = new Date(endDate);
        if (end < start) {
            alert("공연 종료일은 시작일 이후여야 합니다.");
            return;  // 함수 종료하여 API 호출을 막음
        }
        // API 호출
        fetchPerformances(performName, startDate, endDate, locationCode);

        modal.show();  // 모달 띄우기
    });

    function fetchPerformances(performName, startDate, endDate, locationCode) {
        // API URL 설정
        const apiUrl = `/event/search?performName=${performName}&startDate=${startDate}&endDate=${endDate}&locationCode=${locationCode}`;

        // API 호출
        fetch(apiUrl)
            .then(response => response.text())  // XML 데이터를 문자열로 받아옴
            .then(xmlString => {
                // XML 파싱
                const parser = new DOMParser();
                const xmlDoc = parser.parseFromString(xmlString, "text/xml");

                // 공연 데이터 처리 (db 태그를 사용)
                const performances = xmlDoc.getElementsByTagName("db");
                console.log(performances);
                // 기존 테이블 내용 삭제
                const eventListContainer = document.getElementById('eventList');  // 테이블이 들어갈 컨테이너
                eventListContainer.innerHTML = '';

                if (performances.length > 1) {
                    Array.from(performances).forEach(performance => {
                        const row = document.createElement("tr");

                        // 각 공연의 데이터를 추출
                        const poster = performance.getElementsByTagName("poster")[0]?.textContent || '';
                        const mt20id = performance.getElementsByTagName("mt20id")[0]?.textContent || '';
                        const prfnm = performance.getElementsByTagName("prfnm")[0]?.textContent || '';
                        const prfpdfrom = performance.getElementsByTagName("prfpdfrom")[0]?.textContent || '';
                        const prfpdto = performance.getElementsByTagName("prfpdto")[0]?.textContent || '';
                        const area = performance.getElementsByTagName("area")[0]?.textContent || '';
                        const genrenm = performance.getElementsByTagName("genrenm")[0]?.textContent || '';

                        // 테이블 행에 데이터 삽입
                        row.innerHTML = `
                    <td><img src="${poster}" alt="썸네일" style="width: 100%; height: auto;"></td>
                    <td>${mt20id}</td>
                    <td>${prfnm}</td>
                    <td>${prfpdfrom}</td>
                    <td>${prfpdto}</td>
                    <td>${area}</td>
                    <td>${genrenm}</td>
                    <td><button class="btn btn-success btn-sm">추가</button></td>
                `;

                        // 테이블에 추가
                        eventListContainer.appendChild(row);
                    });
                } else {
                    const noDataRow = document.createElement("tr");
                    noDataRow.innerHTML = `<td colspan="8" class="text-center">검색된 공연이 없습니다.</td>`;
                    eventListContainer.appendChild(noDataRow);
                }
            })
            .catch(error => {
                console.error('API 호출 실패:', error);
                alert('공연 목록을 가져오는 데 실패했습니다.');
            });
    }
});
