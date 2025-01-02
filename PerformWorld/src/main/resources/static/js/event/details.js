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
            console.error('Failed to fetch event details:', e);
        }
    }

    getEventDetails(eventId).then(data => {
        if (!data) {
            console.error('No event details found');
            return;
        }

        // 동적으로 데이터 렌더링
        document.getElementsByClassName('poster').src = data.poster; // 포스터 이미지
        document.getElementsByClassName('poster').alt = `${data.title} 포스터`;
        document.getElementsByClassName('title').textContent = data.title; // 공연 제목
        document.getElementsByClassName('description').textContent = data.description || '상세 설명 없음'; // 공연 설명
        document.getElementsByClassName('genre').textContent = `장르: ${data.genreName}`; // 공연 장르
        document.getElementsByClassName('date').textContent = `공연 기간: ${data.prfpdfrom} ~ ${data.prfpdto}`; // 공연 날짜
        document.getElementsByClassName('location').textContent = `공연 장소: ${data.fcltynm}`; // 공연 장소
    });
});
