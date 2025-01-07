// 1. 섹션 수정 모달 열기
async function openEditSectionModal() {
    try {
        // 서버에서 섹션 목록 받아오기
        const response = await axios.get('/admin/sections');
        const sections = response.data;

        const sectionDropdown = document.getElementById('seatSection'); // 올바른 ID 참조
        sectionDropdown.innerHTML = ""; // 기존 목록 초기화

        // 섹션 목록을 드롭다운에 추가
        sections.forEach(section => {
            const option = document.createElement("option");
            option.value = section.section; // section은 SeatDTO의 section 필드
            option.textContent = section.section; // 섹션만 표시 (가격 제거)
            option.dataset.price = section.price; // 가격 정보를 data 속성에 저장
            sectionDropdown.appendChild(option);
        });

        // 첫 번째 섹션 선택 후 가격 자동 채우기 (첫 번째 섹션 선택 시)
        if (sections.length > 0) {
            const firstSection = sections[0].section;
            loadPriceForSection(firstSection, sections);  // 첫 번째 섹션의 가격을 로드
        }

        // 모달 열기
        document.getElementById('editSectionModal').style.display = 'block';
    } catch (error) {
        console.error('섹션 목록을 불러오는 데 오류 발생:', error);
        alert('섹션 목록을 불러오는 데 실패했습니다.');
    }
}

// 섹션을 선택하면 가격을 가져와서 input에 표시
function loadPriceForSection(section, sections) {
    try {
        // 섹션에 해당하는 가격을 sections 배열에서 찾기
        const selectedSection = sections.find(s => s.section === section);
        const price = selectedSection ? selectedSection.price : 0; // 섹션에 해당하는 가격 추출

        // 가격을 입력란에 자동으로 채워넣기
        document.getElementById('seatPrice').value = price;
    } catch (error) {
        console.error('가격을 불러오는 데 오류 발생:', error);
        alert('가격을 불러오는 데 실패했습니다.');
    }
}

// 섹션 변경 시, 가격 자동 갱신
document.getElementById('seatSection').addEventListener('change', function () {
    const selectedSection = this.value;
    const sections = Array.from(document.getElementById('seatSection').options).map(option => ({
        section: option.value,
        price: option.dataset.price // data 속성에서 가격 정보 가져오기
    }));

    loadPriceForSection(selectedSection, sections);
});

// 2. 모달 닫기
function closeSectionModal() {
    // 모달 닫기
    document.getElementById('editSectionModal').style.display = 'none';
}

// 3. 섹션 수정 폼 제출 시
document.getElementById('editSectionForm').addEventListener('submit', async function (event) {
    event.preventDefault(); // 기본 폼 제출 방지

    // 선택한 섹션 및 가격 가져오기
    const selectedSection = document.getElementById('seatSection').value;
    let price = document.getElementById('seatPrice').value;

    // 가격을 Long 형식으로 변환
    price = parseInt(price, 10); // 'Long'에 맞게 정수로 변환

    // 유효성 검사
    if (isNaN(price)) {
        alert('가격은 유효한 숫자여야 합니다.');
        return;
    }

    try {
        // 서버에 수정된 섹션과 가격 전송 (PUT 요청)
        const response = await axios.put('/admin/section', {
            seatId: null,  // seatId는 필요 없으면 null로 전달
            section: selectedSection,
            price: price
        }, {
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.status === 200) {
            alert(`${selectedSection} 섹션 수정이 완료되었습니다.`);
            closeSectionModal(); // 모달 닫기
            // 필요 시 테이블 갱신 로직 추가
            location.reload();
        }
    } catch (error) {
        console.error("섹션 수정 오류:", error);
        if (error.response) {
            console.error("서버 응답 데이터:", error.response.data);
        }
        alert("섹션 수정에 실패했습니다.");
    }
});
