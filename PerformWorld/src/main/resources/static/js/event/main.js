document.addEventListener('DOMContentLoaded', function () {
    let categoryId = "";

    // 초기 이벤트 목록 가져오기
    getEventsList().then(data => {
        updateEventList(data); // 초기 데이터를 화면에 렌더링
    });

    // 공연목록 조회
    async function getEventsList() {
        const res = await axios({
            method: 'get',
            url: `/event/savedEventList?title=${document.querySelector("input[name='srhEvent']").value}&genre=${categoryId}`,
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return res.data;
    }

    // 카테고리 목록 조회
    async function getCateList() {
        const res = await axios({
            method: 'post',
            url: '/sys/getList',
            data: { mainCode: 'CTG' },
            headers: { 'Content-Type': 'application/json' },
        });
        return res.data;
    }

    // 공연목록 그리기
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
            aTag.style.backgroundImage = `url('${item.thumbnailUrl}')`;

            const descDiv = document.createElement('div');
            descDiv.classList.add('desc');
            descDiv.style.width = '300px';
            descDiv.style.height = '450px';

            const h3Tag = document.createElement('h3');
            h3Tag.textContent = `${item.title}`;

            const spanTag = document.createElement('span');
            spanTag.textContent = `${item.genre}`;

            descDiv.appendChild(h3Tag);
            descDiv.appendChild(spanTag);
            aTag.appendChild(descDiv);
            colDiv.appendChild(aTag);

            container.appendChild(colDiv);
        });
    }

    // 카테고리 목록 가져오기 및 처리
    getCateList().then(data => {
        const ulElement = document.querySelector(".header_genreMenu__34716");

        // "전체" 카테고리 추가
        ulElement.innerHTML = `<li class="categoryList active" data-id="">
                              <a class="header_link__5ddf2">전체</a>
                           </li>`;
        // 카테고리 목록 추가
        for (const cate of data) {
            ulElement.innerHTML += `<li class="categoryList" data-id="${cate.code}">
                                 <a class="header_link__5ddf2">${cate.codeName}</a>
                              </li>`;
        }

        // 카테고리 클릭 이벤트
        document.querySelectorAll('.categoryList').forEach(item => {
            item.addEventListener('click', () => {
                categoryId = item.dataset.id; // 선택한 카테고리 ID 가져오기

                // 기존 active 클래스 제거
                document.querySelectorAll('.categoryList').forEach(i => {
                    i.classList.remove('active');
                });

                // 클릭된 카테고리에 active 클래스 추가
                item.classList.add('active');

                getEventsList().then(data => {
                    updateEventList(data);
                });
            });
        });
    });

    // top 버튼
    const topButton = document.querySelector(".topButton");

    topButton.addEventListener("click", function(){
        window.scrollTo({
            top: 0,
            behavior: "smooth" // 부드러운 스크롤
        });
    });

    window.addEventListener("scroll", function () {
        if (document.body.scrollTop > 200 || document.documentElement.scrollTop > 200) {
            topButton.style.display = "block"; // 스크롤 200px 이상에서 표시
        } else {
            topButton.style.display = "none"; // 숨김
        }
    });

    // 공연 등록
    document.querySelector(".eventRegisterBtn")?.addEventListener("click",function (e){
        e.stopPropagation();
        e.preventDefault();

        window.location.href="/event/register";
    });

    // 검색
    document.querySelector("button[name='searchButton']").addEventListener("click", function () {
        getEventsList().then(data => {
            console.log(data)
            updateEventList(data);
        });
    });
});