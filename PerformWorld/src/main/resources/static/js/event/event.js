document.addEventListener("DOMContentLoaded", function () {
    const modal = new bootstrap.Modal(document.querySelector(".eventRegisterModal"));
    const searchEventBtn = document.querySelector(".searchEventBtn");
    const eventListContainer = document.getElementById("eventList");

    const prevPageBtn = document.getElementById("prevPageBtn");
    const nextPageBtn = document.getElementById("nextPageBtn");
    const pageButtons = document.getElementById("pageButtons");
    const pageInput = document.getElementById("pageInput");

    // 페이지 크기 (한 페이지에 10개 항목)
    const pageSize = 10;
    let currentPage = 1; // 현재 페이지

    searchEventBtn.addEventListener("click", function (e) {
        e.preventDefault();
        e.stopPropagation();
        currentPage = 1;
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
        fetchPerformances(performName, startDate, endDate, locationCode, currentPage);

        modal.show();  // 모달 띄우기
    });

    function fetchPerformances(performName, startDate, endDate, locationCode, page) {
        // API URL 설정
        const apiUrl = `/event/search?performName=${performName}&startDate=${startDate}&endDate=${endDate}&locationCode=${locationCode}&page=${page}&size=${pageSize}`;
        console.log("현재페이지 : " + page)
        fetch(apiUrl)
            .then(response => response.text())  // XML 데이터를 문자열로 받아옴
            .then(xmlString => {
                // XML 파싱
                const parser = new DOMParser();
                const xmlDoc = parser.parseFromString(xmlString, "text/xml");

                // 공연 데이터 처리 (db 태그를 사용)
                const performances = xmlDoc.getElementsByTagName("db");
                // 기존 테이블 내용 삭제
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
                    <td><button class="btn btn-success btn-sm add-btn" data-id="${mt20id}">추가</button></td>
                `;
                        // 테이블에 추가
                        eventListContainer.appendChild(row);
                    });
                    // 페이지 네비게이션 버튼 활성화
                    updatePagination(performances.length);
                } else {
                    const noDataRow = document.createElement("tr");
                    noDataRow.innerHTML = `<td colspan="8" class="text-center">검색된 공연이 없습니다.</td>`;
                    eventListContainer.appendChild(noDataRow);
                    updatePagination(0);
                }
                eventListContainer.scrollIntoView({behavior: "smooth", block: "start"});
            })
            .catch(error => {
                console.error('API 호출 실패:', error);
                alert('공연 목록을 가져오는 데 실패했습니다.');
            });
    }

    function updatePagination(performancesLength) {
        // 페이지 네비게이션 버튼을 업데이트
        // 이전 버튼 활성화 여부
        prevPageBtn.disabled = currentPage === 1;

        nextPageBtn.disabled = performancesLength < pageSize;

        // 페이지 번호 버튼들 업데이트
        pageButtons.textContent = `Page ${currentPage}`;
    }

    // 페이지 이동 버튼 이벤트
    prevPageBtn.addEventListener("click", function () {
        if (currentPage > 1) {
            currentPage -= 1;
            triggerFetch();
        }
    });

    nextPageBtn.addEventListener("click", function () {
        currentPage += 1;
        triggerFetch();
    });

    // 페이지 번호 입력 필드 이벤트
    pageInput.addEventListener("keydown", function (event) {
        if (event.key === "Enter") {
            const inputPage = parseInt(pageInput.value, 10);
            if (inputPage > 0 && inputPage !== currentPage) {
                currentPage = inputPage;
                triggerFetch();
                pageInput.value = "";
            }
        }
    });

    // API 호출 트리거 함수
    function triggerFetch() {
        fetchPerformances(
            document.querySelector("input[name='perform-name']").value,
            document.querySelector("input[name='start-date']").value,
            document.querySelector("input[name='end-date']").value,
            document.querySelector("select[name='location-code']").value,
            currentPage
        );

        // 테이블 맨 위로 스크롤
        eventListContainer.scrollIntoView({behavior: "smooth", block: "start"});
    }

    eventListContainer.addEventListener("click", function (e) {
        if (e.target.classList.contains("add-btn")) {
            const eventID = e.target.getAttribute("data-id");
            console.log("eventID:" + eventID)
            if (eventID) {
                fetchEventDetails(eventID)
                    .then((eventData) => saveEvent(eventData))
                    .catch((error) => {
                        console.error("상세 조회 또는 저장 실패:", error);
                        alert(error);
                        alert("저장에 실패했습니다.");
                    });
            }
        }
    });

    async function fetchEventDetails(eventID) {

        const apiUrl = `/event/detail/${eventID}`;

        const response = await fetch(apiUrl);
        if (!response.ok) {
            throw new Error(`상세 조회 실패: ${response.status}`);
        }

        const xmlString = await response.text();
        const parser = new DOMParser();
        const xmlDoc = parser.parseFromString(xmlString, "text/xml");

        console.log("상세 조회 결과:", xmlDoc);

        // 필요한 데이터 추출
        const genrenm = xmlDoc.getElementsByTagName("genrenm")[0]?.textContent || '';
        const prfnm = xmlDoc.getElementsByTagName("prfnm")[0]?.textContent || '';
        const prfpdfrom = xmlDoc.getElementsByTagName("prfpdfrom")[0]?.textContent || '';
        const prfpdto = xmlDoc.getElementsByTagName("prfpdto")[0]?.textContent || '';
        const prfcast = xmlDoc.getElementsByTagName("prfcast")[0]?.textContent || '';
        const area = xmlDoc.getElementsByTagName("area")[0]?.textContent || '';
        const prfruntime = xmlDoc.getElementsByTagName("prfruntime")[0]?.textContent || '';
        const poster = xmlDoc.getElementsByTagName("poster")[0]?.textContent || '';

        // styurls는 여러 개일 수 있으므로 배열로 처리
        const styurls = Array.from(xmlDoc.getElementsByTagName("styurl")).map(url => url.textContent);

        // 필요한 데이터를 객체로 반환
        return {
            genrenm,
            prfnm,
            prfpdfrom,
            prfpdto,
            prfcast,
            area,
            prfruntime,
            poster,
            styurls
        };
    }

    async function saveEvent(eventData) {
        const saveApiUrl = "/event/save";

        // XML 데이터 생성
        const xmlString = `
    <event>
        <genrenm>${eventData.genrenm || ''}</genrenm>
        <prfnm>${eventData.prfnm || ''}</prfnm>
        <prfpdfrom>${eventData.prfpdfrom || ''}</prfpdfrom>
        <prfpdto>${eventData.prfpdto || ''}</prfpdto>
        <prfcast>${eventData.prfcast || ''}</prfcast>
        <area>${eventData.area || ''}</area>
        <prfruntime>${eventData.prfruntime || ''}</prfruntime>
        <poster>${eventData.poster || ''}</poster>
        <styurls>
            ${eventData.styurls && eventData.styurls.length > 0
            ? eventData.styurls.map(url => `<styurl>${url}</styurl>`).join('')
            : ''}
        </styurls>
    </event>
`;

        console.log("결과"+xmlString);
        // XML 형식으로 서버에 전송
        const response = await fetch(saveApiUrl, {
            method: "POST",
            headers: { "Content-Type": "application/xml" },
            body: xmlString, // 생성된 XML 데이터를 요청 본문에 포함
        });

        if (!response.ok) {
            const errorText = await response.text();  // 응답 본문을 텍스트로 읽기
            throw new Error(`저장 실패: ${response.status} - ${errorText}`);
        }

        alert("이벤트가 성공적으로 저장되었습니다.");
    }

    document.addEventListener("DOMContentLoaded", function() {
        console.log("페이지 로딩됨!");

        // 페이지가 로드되면 이벤트 데이터를 가져옵니다.
        fetch('/event/savedEventList')  // 위에서 만든 REST API URL
            .then(response => response.json())  // 서버에서 반환한 JSON 데이터를 파싱합니다.
            .then(events => {
                const savedEventList = document.getElementById("savedEventList");

                // 받아온 이벤트 데이터로 테이블을 채웁니다.
                events.forEach(event => {
                    const row = document.createElement("tr");

                    // 썸네일 이미지 (Image URL)
                    const thumbnailCell = document.createElement("td");
                    const thumbnailImg = document.createElement("img");
                    thumbnailImg.src = event.poster;  // 'poster' 필드는 이미지 URL입니다.
                    thumbnailImg.alt = "Thumbnail";
                    thumbnailImg.style.width = "50px";  // 적당한 크기로 조정
                    thumbnailImg.style.height = "50px";
                    thumbnailCell.appendChild(thumbnailImg);

                    // Event ID
                    const eventIdCell = document.createElement("td");
                    eventIdCell.textContent = event.eventId;

                    // 제목
                    const titleCell = document.createElement("td");
                    titleCell.textContent = event.title;

                    // 공연 시작일
                    const prfpdfromCell = document.createElement("td");
                    prfpdfromCell.textContent = event.prfpdfrom;

                    // 공연 종료일
                    const prfpdtoCell = document.createElement("td");
                    prfpdtoCell.textContent = event.prfpdto;

                    // 지역
                    const locationCell = document.createElement("td");
                    locationCell.textContent = event.location;

                    // 장르
                    const genreCell = document.createElement("td");
                    genreCell.textContent = event.genreName;  // 장르 이름

                    // 삭제 (버튼)
                    const deleteCell = document.createElement("td");
                    const deleteButton = document.createElement("button");
                    deleteButton.textContent = "삭제";
                    deleteButton.classList.add("btn", "btn-danger");
                    deleteButton.onclick = function() {
                        // 삭제 처리 코드 추가
                        deleteEvent(event.eventId);
                    };
                    deleteCell.appendChild(deleteButton);

                    // 각 셀을 행에 추가
                    row.appendChild(thumbnailCell);
                    row.appendChild(eventIdCell);
                    row.appendChild(titleCell);
                    row.appendChild(prfpdfromCell);
                    row.appendChild(prfpdtoCell);
                    row.appendChild(locationCell);
                    row.appendChild(genreCell);
                    row.appendChild(deleteCell);

                    // 테이블 본문에 행 추가
                    savedEventList.appendChild(row);
                });
            })
            .catch(error => {
                console.error("이벤트 데이터를 가져오는 중 오류 발생:", error);
            });
    });

// 이벤트 삭제 함수
    function deleteEvent(eventId) {
        fetch(`/event/deleteEvent/${eventId}`, {
            method: 'DELETE',  // DELETE 요청
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);  // 성공 메시지 출력
                alert('이벤트가 삭제되었습니다.');
                location.reload();  // 삭제 후 페이지 새로고침
            })
            .catch(error => {
                console.error("삭제 중 오류 발생:", error);
            });
    }



});
