document.addEventListener('DOMContentLoaded', function() {

    async function getEventsList() {
        try {
            const res = await axios({
                method: 'get',
                url: '/event/savedEventList',
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log(res)
            return res.data;
        } catch (e) {
            console.error(e)
        }
    }

    getEventsList().then(data => {
        console.log(data)
        let container = document.getElementById('project-list');  // 항목이 들어갈 부모 요소

        data.forEach(item => {
            // 각 항목을 위한 div 생성
            let colDiv = document.createElement('div');
            colDiv.classList.add('col-md-3', 'col-sm-6', 'col-padding', 'text-center', 'animate-box');

            let aTag = document.createElement('a');
            aTag.href = `/event/details/${item.eventId}`;
            aTag.classList.add('work', 'image-popup');
            aTag.style.backgroundImage = `url('${item.poster}')`;  // 포스터 이미지 URL 설정


            let descDiv = document.createElement('div');
            descDiv.classList.add('desc');

            let h3Tag = document.createElement('h3');
            h3Tag.textContent = `${item.title}`;  // 공연 이름

            let spanTag = document.createElement('span');
            spanTag.textContent = `${item.genreName}`;  // 공연 장르

            // HTML 요소를 결합
            descDiv.appendChild(h3Tag);
            descDiv.appendChild(spanTag);
            aTag.appendChild(descDiv);
            colDiv.appendChild(aTag);

            // 생성된 div를 부모 컨테이너에 추가
            container.appendChild(colDiv);
        });
    })
});