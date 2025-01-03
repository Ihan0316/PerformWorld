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
        const genre = document.querySelector("select[name='genre-select']").value;

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
        fetchPerformances(performName, startDate, endDate, locationCode, genre, currentPage);

        modal.show();  // 모달 띄우기
    });

    function fetchPerformances(performName, startDate, endDate, locationCode, genre, page) {
        // API URL 설정
        const apiUrl = `/event/search?performName=${performName}&startDate=${startDate}&endDate=${endDate}&locationCode=${locationCode}&genre=${genre}&page=${page}&size=${pageSize}`;
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

                if (performances.length > 0) {
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

    function triggerFetch() {
        fetchPerformances(
            document.querySelector("input[name='perform-name']").value,
            document.querySelector("input[name='start-date']").value,
            document.querySelector("input[name='end-date']").value,
            document.querySelector("select[name='location-code']").value,
            document.querySelector("select[name='genre-select']").value,
            currentPage
        );

        // 테이블 맨 위로 스크롤
        eventListContainer.scrollIntoView({behavior: "smooth", block: "start"});
    }

    eventListContainer.addEventListener("click", function (e) {
        if (e.target.classList.contains("add-btn")) {
            const eventID = e.target.getAttribute("data-id");
            if (eventID) {
                fetchEventDetails(eventID)
                    .then((eventData) => {
                        saveEvent(eventData)
                    })
                    .catch((error) => {
                        console.error("상세 조회 또는 저장 실패:", error);
                        // alert(error);
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

        // 필요한 데이터 추출
        const mt20id = xmlDoc.getElementsByTagName("mt20id")[0]?.textContent || '';
        const genrenm = xmlDoc.getElementsByTagName("genrenm")[0]?.textContent || '';
        const prfnm = xmlDoc.getElementsByTagName("prfnm")[0]?.textContent || '';
        const prfpdfrom = xmlDoc.getElementsByTagName("prfpdfrom")[0]?.textContent || '';
        const prfpdto = xmlDoc.getElementsByTagName("prfpdto")[0]?.textContent || '';
        const prfcast = xmlDoc.getElementsByTagName("prfcast")[0]?.textContent || '';
        const fcltynm = xmlDoc.getElementsByTagName("fcltynm")[0]?.textContent || '';
        const prfruntime = xmlDoc.getElementsByTagName("prfruntime")[0]?.textContent || '';
        const dtguidance = xmlDoc.getElementsByTagName("dtguidance")[0]?.textContent || '';
        const poster = xmlDoc.getElementsByTagName("poster")[0]?.textContent || '';
        // styurls는 여러 개일 수 있으므로 배열로 처리
        const styurls = Array.from(xmlDoc.getElementsByTagName("styurl")).map(url => url.textContent);

        // 필요한 데이터를 객체로 반환
        return {
            mt20id,
            genrenm,
            prfnm,
            prfpdfrom,
            prfpdto,
            prfcast,
            fcltynm,
            prfruntime,
            dtguidance,
            poster,
            styurls
        };
    }

    async function saveEvent(eventData) {
        const saveApiUrl = "/event/save";

        // XML 데이터 생성
        const xmlString = `
    <event>
        <mt20id>${eventData.mt20id || ''}</mt20id>
        <genrenm>${eventData.genrenm || ''}</genrenm>
        <prfnm>${eventData.prfnm || ''}</prfnm>
        <prfpdfrom>${eventData.prfpdfrom || ''}</prfpdfrom>
        <prfpdto>${eventData.prfpdto || ''}</prfpdto>
        <prfcast>${eventData.prfcast || ''}</prfcast>
        <fcltynm>${eventData.fcltynm || ''}</fcltynm>
        <prfruntime>${eventData.prfruntime || ''}</prfruntime>
        <dtguidance>${eventData.dtguidance || ''}</dtguidance>
        <poster>${eventData.poster || ''}</poster>
        <styurls>
            ${eventData.styurls && eventData.styurls.length > 0
            ? eventData.styurls.map(url => `<styurl>${url}</styurl>`).join('')
            : ''}
        </styurls>
    </event>
`;
        console.log("필요한 데이터3:"+xmlString)
        // XML 형식으로 서버에 전송
        const response = await fetch(saveApiUrl, {
            method: "POST",
            headers: { "Content-Type": "application/xml" },
            body: xmlString, // 생성된 XML 데이터를 요청 본문에 포함
        });

        if (!response.ok) {
            const errorText = await response.text();  // 응답 본문을 텍스트로 읽기
            alert("저장에 실패 했습니다. "+ errorText)
            throw new Error(`저장 실패: ${response.status} - ${errorText}`);

        }
        alert("공연을 저장했습니다.")
    }

    async function getRegionList() {
        const res = await axios({
            method: 'post',
            url: '/sys/getList',
            data: { mainCode: 'RGN' },  // 장르 카테고리
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return res.data;
    }

    getRegionList().then(data => {
        console.log(data);
        const selectElement = document.querySelector("select[name='location-code']");
        const optionElement = document.createElement("option");
        optionElement.value = ""
        optionElement.textContent = "지역 선택";  // 이름
        selectElement.appendChild(optionElement);
        for(const region of data) {
            const optionElement = document.createElement("option");
            optionElement.value = region.code;  // 코드
            optionElement.textContent = region.codeName;  // 이름

            selectElement.appendChild(optionElement);
        }
    }).catch(e => {
        console.error(e);
    });

    async function getCategoryList() {
        const res = await axios({
            method: 'post',
            url: '/sys/getList',
            data: { mainCode: 'CTG' },  // 장르 카테고리
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return res.data;
    }
    getCategoryList().then(data => {
        console.log(data);
        const selectElement = document.querySelector("select[name='genre-code']");
        const optionElement = document.createElement("option");
        optionElement.value = ""
        optionElement.textContent = "장르 선택";  //
        selectElement.appendChild(optionElement);
        for(const genre of data) {
            const optionElement = document.createElement("option");
            optionElement.value = genre.code;  // 코드
            optionElement.textContent = genre.codeName;  // 이름

            selectElement.appendChild(optionElement);
        }
    }).catch(e => {
        console.error(e);
    });

    async function getCategoryList2() {
        const res = await axios({
            method: 'post',
            url: '/sys/getList',
            data: { mainCode: 'CTG' },  // 장르 카테고리
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return res.data;
    }
    getCategoryList2().then(data => {
        console.log(data);
        const selectElement = document.querySelector("select[name='genre-select']");
        const optionElement = document.createElement("option");
        optionElement.value = ""
        optionElement.textContent = "장르 선택";  //
        selectElement.appendChild(optionElement);
        for(const category of data) {
            const optionElement = document.createElement("option");
            optionElement.value = category.code;  // 코드
            optionElement.textContent = category.codeName;  // 이름

            selectElement.appendChild(optionElement);
        }
    }).catch(e => {
        console.error(e);
    });
});
