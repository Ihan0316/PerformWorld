// const container = document.querySelector('.fh5co-narrow-content .row'); // 프로젝트들이 들어갈 부모 div
//
// for (let i = 0; i < performances.length; i++) {
//     const performance = performances[i];
//
//     // 새 프로젝트 항목을 위한 div 요소 생성
//     const projectDiv = document.createElement('div');
//     projectDiv.classList.add('col-md-3', 'col-sm-6', 'col-padding', 'text-center', 'animate-box');
//
//     // 링크 요소 생성
//     const link = document.createElement('a');
//     link.href = '#';
//     link.classList.add('work', 'image-popup');
//     link.style.backgroundImage = `url(${performance.poster})`;
//
//     // 설명 요소 생성
//     const descDiv = document.createElement('div');
//     descDiv.classList.add('desc');
//
//     // 제목과 카테고리 텍스트 생성
//     const title = document.createElement('h3');
//     title.textContent = performance.prfnm;
//
//     const category = document.createElement('span');
//     category.textContent = performance.genrenm;
//
//     // 공연 기간 정보 추가
//     const period = document.createElement('p');
//     period.textContent = `${performance.prfpdfrom} - ${performance.prfpdto}`;
//
//     // 장소 정보 추가
//     const location = document.createElement('p');
//     location.textContent = performance.fcltynm + ', ' + performance.area;
//
//     // 공연 상태 추가
//     const status = document.createElement('p');
//     status.textContent = performance.prfstate;
//
//     // 설명 요소에 제목, 카테고리, 기간, 장소, 상태 추가
//     descDiv.appendChild(title);
//     descDiv.appendChild(category);
//     descDiv.appendChild(period);
//     descDiv.appendChild(location);
//     descDiv.appendChild(status);
//
//     // 링크 요소에 설명 추가
//     link.appendChild(descDiv);
//
//     // 최종적으로 div 요소에 링크 추가
//     projectDiv.appendChild(link);
//
//     // 부모 요소에 새로 생성된 항목 추가
//     container.appendChild(projectDiv);
// }

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
