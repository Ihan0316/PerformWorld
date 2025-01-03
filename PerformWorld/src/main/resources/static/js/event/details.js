document.addEventListener('DOMContentLoaded', function () {

    async function getEventDetails(eventId) {
        try {
            const res = await axios({
                method: 'post',
                url: `/event/details/${eventId}`, // 서버에서 eventId로 데이터 가져오기
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            return res.data; // 서버로부터 데이터 반환
        } catch (e) {
            console.error('event에 데이터가 없음:', e);
        }
    }

    getEventDetails(eventId).then(data => {
        if (!data) {
            console.error('event details를 찾을수 없음');
            return;
        }
        console.log(data)
        // 동적으로 데이터 렌더링
        const posterImg = document.querySelector('.poster'); // 포스터 이미지
        const titleElement = document.querySelector('.title'); // 공연 제목
        const descriptionElement = document.querySelector('.description'); // 공연 설명
        const genreElement = document.querySelector('.genre'); // 공연 장르
        const dateElement = document.querySelector('.date'); // 공연 날짜
        const locationElement = document.querySelector('.location'); // 공연 장소
        const detailsContainer = document.querySelector('.details-container');

        // 데이터 적용
        const elements = [
            { el: posterImg, key: 'poster', action: (el, value) => { el.src = value || ''; el.alt = `${data.title || '제목 없음'} 포스터`; } },
            { el: titleElement, key: 'title', action: (el, value) => { el.textContent = value || '제목 없음'; } },
            { el: descriptionElement, key: 'description', action: (el, value) => { el.textContent = value || '상세 설명 없음'; } },
            { el: genreElement, key: 'genreName', action: (el, value) => { el.textContent = `장르: ${value || '정보 없음'}`; } },
            { el: dateElement, key: 'prfpdfrom', action: (el, value) => { el.textContent = `공연 기간: ${value || '알 수 없음'} ~ ${data.prfpdto || '알 수 없음'}`; } },
            { el: locationElement, key: 'fcltynm', action: (el, value) => { el.textContent = `공연 장소: ${value || '정보 없음'}`; } }
        ];

        elements.forEach(({ el, key, action }) => {
            if (el && data[key] !== undefined) {
                action(el, data[key]);
            }
        });

        // 상세 이미지 렌더링
        if (detailsContainer && data.imageUrls && data.imageUrls.length > 0) {
            data.imageUrls.forEach(url => {
                const img = document.createElement('img');
                img.src = url;
                img.alt = '상세 이미지';
                img.className = 'detail-image';
                detailsContainer.appendChild(img);
            });
        }
    });
});
