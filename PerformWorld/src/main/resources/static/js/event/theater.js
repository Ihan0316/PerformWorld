document.addEventListener('DOMContentLoaded', function () {
    async function getEventsList() {
        try {
            const res = await axios({
                method: 'get',
                url: '/event/getEventList',
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            return res.data;
        } catch (e) {
            console.error('Error fetching events:', e);
        }
    }

    async function getCateList() {
        try {
            const res = await axios({
                method: 'post',
                url: '/sys/getList',
                data: { mainCode: 'CTG' },
                headers: { 'Content-Type': 'application/json' },
            });
            return res.data;
        } catch (error) {
            console.error("Failed to fetch categories:", error);
        }
    }

    async function getCategoryDetails(genre) {
        try {
            const res = await axios({
                method: 'get',
                url: `/event/category/${genre}`,
                headers: { 'Content-Type': 'application/json' },
            });

            if (res.status === 200) {
                console.log(`Genre ${genre} details:`, res.data);
                updateEventList(res.data); // 데이터를 화면에 렌더링
            } else if (res.status === 404) {
                alert('선택한 장르에 대한 데이터가 없습니다.');
            }
        } catch (error) {
            console.error(`Failed to fetch details for genre ${genre}:`, error);
        }
    }

    function updateEventList(data) {
        const container = document.getElementById('project-list');
        container.innerHTML = ''; // 기존 목록 초기화

        data.forEach(item => {
            const colDiv = document.createElement('div');
            colDiv.classList.add('col-padding', 'text-center', 'animate-box');
            colDiv.style.width = '300px';
            colDiv.style.height = '500px';

            const aTag = document.createElement('a');
            aTag.href = `/event/details/${item.eventId}`;
            aTag.classList.add('work', 'image-popup');
            aTag.style.backgroundImage = `url('${item.poster}')`;

            const descDiv = document.createElement('div');
            descDiv.classList.add('desc');
            descDiv.style.width = '300px';
            descDiv.style.height = '450px';

            const h3Tag = document.createElement('h3');
            h3Tag.textContent = `${item.title}`;

            const spanTag = document.createElement('span');
            spanTag.textContent = `${item.genreName}`;

            descDiv.appendChild(h3Tag);
            descDiv.appendChild(spanTag);
            aTag.appendChild(descDiv);
            colDiv.appendChild(aTag);

            container.appendChild(colDiv);
        });
    }

    // 카테고리 목록 가져오기 및 처리
    getCateList().then(data => {
        console.log(data);
        const ulElement = document.querySelector(".header_genreMenu__34716");

        // "전체" 카테고리 추가
        ulElement.innerHTML = `<li class="categoryList" data-id="all">
                              <a class="header_link__5ddf2">전체</a>
                           </li>`;

        // 카테고리 목록 추가
        for (const cate of data) {
            ulElement.innerHTML += `<li class="categoryList" data-id="${cate.code}">
                                 <a class="header_link__5ddf2">${cate.codeName}</a>
                              </li>`;
        }

        // 클릭 이벤트 추가
        document.querySelectorAll('.categoryList').forEach(item => {
            item.addEventListener('click', () => {
                const categoryId = item.dataset.id; // 선택한 장르 ID 가져오기
                console.log(`Selected Category: ${categoryId}`);
                getCategoryDetails(categoryId); // 선택된 장르 데이터 요청
            });
        });
    });

    // 초기 이벤트 목록 가져오기
    getEventsList().then(data => {
        if (!data || data.length === 0) {
            console.error('No event data received');
            return;
        }
        updateEventList(data); // 초기 데이터를 화면에 렌더링
    });
});
