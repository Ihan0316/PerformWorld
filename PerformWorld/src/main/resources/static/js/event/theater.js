document.addEventListener('DOMContentLoaded', function() {
    // API에서 데이터를 가져와서 HTML을 동적으로 생성
    fetch('API_URL')  // 실제 API URL을 입력하세요.
        .then(response => response.json())
        .then(data => {
            let container = document.getElementById('project-list');  // 항목이 들어갈 부모 요소

            data.forEach(item => {
                // 각 항목을 위한 div 생성
                let colDiv = document.createElement('div');
                colDiv.classList.add('col-md-3', 'col-sm-6', 'col-padding', 'text-center', 'animate-box');

                let aTag = document.createElement('a');
                aTag.href = "#";
                aTag.classList.add('work', 'image-popup');
                aTag.style.backgroundImage = `url(${item.poster})`;  // 포스터 이미지 URL 설정

                let descDiv = document.createElement('div');
                descDiv.classList.add('desc');

                let h3Tag = document.createElement('h3');
                h3Tag.textContent = item.prfnm;  // 공연 이름

                let spanTag = document.createElement('span');
                spanTag.textContent = item.genrenm;  // 공연 장르

                // HTML 요소를 결합
                descDiv.appendChild(h3Tag);
                descDiv.appendChild(spanTag);
                aTag.appendChild(descDiv);
                colDiv.appendChild(aTag);

                // 생성된 div를 부모 컨테이너에 추가
                container.appendChild(colDiv);
            });
        })
        .catch(error => {
            console.error('데이터 로딩 중 오류 발생:', error);
        });
});


// API 데이터를 받아서 HTML을 동적으로 생성하는 코드
fetch('API_URL')  // API URL을 실제 API로 교체하세요
    .then(response => response.json())  // JSON 형식으로 데이터 받기
    .then(data => {
        let container = document.querySelector('.fh5co-narrow-content .row');  // 데이터가 삽입될 부모 컨테이너

        // data 배열을 반복하면서 각 항목에 대해 HTML을 동적으로 생성
        data.forEach(item => {
            // 각 공연에 대한 div 생성
            let colDiv = document.createElement('div');
            colDiv.classList.add('col-md-3', 'col-sm-6', 'col-padding', 'text-center', 'animate-box');

            let aTag = document.createElement('a');
            aTag.href = "#";
            aTag.classList.add('work', 'image-popup');
            aTag.style.backgroundImage = `url(${item.poster})`;  // 포스터 이미지 URL을 스타일로 설정

            let descDiv = document.createElement('div');
            descDiv.classList.add('desc');

            let h3Tag = document.createElement('h3');
            h3Tag.textContent = item.prfnm;  // 공연 이름

            let spanTag = document.createElement('span');
            spanTag.textContent = item.genrenm;  // 공연 장르

            // HTML 요소 결합
            descDiv.appendChild(h3Tag);
            descDiv.appendChild(spanTag);
            aTag.appendChild(descDiv);
            colDiv.appendChild(aTag);

            // 생성된 div를 부모 컨테이너에 추가
            container.appendChild(colDiv);
        });
    })
    .catch(error => {
        console.error('Error fetching the data:', error);
    });
