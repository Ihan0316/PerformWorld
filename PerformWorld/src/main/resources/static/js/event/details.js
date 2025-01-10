document.addEventListener('DOMContentLoaded', function () {

    // 상세정보 조회
    async function getEventDetails(eventId) {
        const res = await axios({
            method: 'post',
            url: `/event/details/${eventId}`, // 서버에서 eventId로 데이터 가져오기
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return res.data;
    }

    // 상세이미지 조회
    async function getDetailImages(eventId) {
        const res = await axios({
            method: 'post',
            url: `/event/details/${eventId}/images`, // 서버에서 eventId로 데이터 가져오기
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return res.data;
    }

    getEventDetails(eventId).then(data => {
        if (!data) {
            console.error('event details를 찾을 수 없음');
            return;
        }

        // 속성-요소 매핑
        const mappings = [
            { selector: '.poster', key: 'poster', apply: (el, value) => { el.src = value || ''; el.alt = `${data.title || '제목 없음'} 포스터`; } },
            { selector: '.title', key: 'title', apply: (el, value) => { el.textContent = value || '제목 없음'; } },
            { selector: '.description', key: 'description', apply: (el, value) => { el.textContent = value || '상세 설명 없음'; } },
            { selector: '.genre', key: 'genreName', apply: (el, value) => { el.textContent = `장르: ${value || '정보 없음'}`; } },
            { selector: '.date', key: 'prfpdfrom', apply: (el, value) => { el.textContent = `공연 기간: ${value || '알 수 없음'} ~ ${data.prfpdto || '알 수 없음'}`; } },
            { selector: '.location', key: 'fcltynm', apply: (el, value) => { el.textContent = `공연장: ${value || '정보 없음'}`; } },
            { selector: '.runtime', key: 'runtime', apply: (el, value) => { el.textContent = `상영 시간: ${value || '정보 없음'}분`; } },
            { selector: '.casting', key: 'casting', apply: (el, value) => { el.textContent = `출연진: ${value === ' ' ? '없음' : value }`; } },
        ];

        // 매핑에 따라 데이터 적용
        mappings.forEach(({ selector, key, apply }) => {
            const element = document.querySelector(selector);
            if (element && data[key] !== undefined) {
                apply(element, data[key]);
            }
        });

        // 상세 이미지 렌더링
        const detailsContainer = document.querySelector('.details-container');
        getDetailImages(eventId).then(res => {
            res.forEach(image => {
                const img = document.createElement('img');
                img.src = image;
                img.alt = '상세 이미지';
                img.className = 'detail-image';
                detailsContainer.appendChild(img);
            });
        }).catch(e => {
            alert("이미지를 불러오는데 실패했습니다.");
        });

    }).catch(e => {
        alert("상세 정보를 가져오는데 실패했습니다.");
    });
});

// top 버튼
const topButton = document.querySelector(".topButton");

topButton.addEventListener("click", function(){
    window.scrollTo({
        top: 0,
        behavior: "smooth" // 부드러운 스크롤
    });
})
window.addEventListener("scroll", function () {
    if (document.body.scrollTop > 200 || document.documentElement.scrollTop > 200) {
        topButton.style.display = "block"; // 스크롤 200px 이상에서 표시
    } else {
        topButton.style.display = "none"; // 숨김
    }
});

// 예매하기
document.querySelector(".bookingBtn")?.addEventListener("click", async function (e) {
    const res = await axios({
        method: 'post',
        url: `/book/getEventTicketing`,
        data : { eventId: eventId },
        headers: {
            'Content-Type': 'application/json'
        }
    });

    if(res.data.length === 0) {
        alert("예매 가능 시간이 아닙니다.");
        return;
    }
    window.location.href = `/event/${eventId}/book`;
});