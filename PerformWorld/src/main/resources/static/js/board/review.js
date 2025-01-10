// grid 초기화
const initGrid = () => {
    const Grid = tui.Grid;

    // 테마
    Grid.applyTheme('clean',  {
        cell: {
            normal: {
                border: 'gray',
                showVerticalBorder: true,
                showHorizontalBorder: true
            },
            header: {
                background: 'gray',
                text: 'white',
                border: 'white'
            }
        }
    });

    // 세팅
    const grid = new Grid({
        el: document.getElementById('rvListGrid'),
        scrollX: false,
        scrollY: false,
        minBodyHeight: 40,
        pageOptions: {
            useClient: true,  // 프론트에서 페이징
            perPage: 5
        },
        columns: [
            {
                header: '번호',
                name: 'reviewId',
                hidden: true
            },
            {
                header: '공연명',
                name: 'eventInfo',
                align: 'center',
                width: 250,
                formatter: (value) => {
                    if (value) {
                        const data = value.value;
                        return data.split(" : ")[0]
                    }
                    return "";
                }
            },
            {
                header: '내용',
                name: 'content',
                minWidth: 200
            },
            {
                header: '작성자',
                name: 'userId',
                align: 'center',
                width: 100
            },
            {
                header: '작성일',
                name: 'regDate',
                align: 'center',
                width: 100,
                formatter: (value) => {
                    if (value) {
                        const data = value.value;
                        return data.split('T')[0];
                    }
                    return "";
                }
            }
        ]
    });

    // 행 더블클릭 시 상세 정보로
    grid.on('dblclick', (e) => {
        const rowKey = e.rowKey;  // 클릭한 행의 rowKey
        if (rowKey !== null) {
            console.log('클릭된 행:', grid.getRow(rowKey));
            const row = grid.getRow(rowKey);

            if(user == null) {
                if(document.querySelector(".updRvBtn")) {
                    document.querySelector(".updRvBtn").style.display = 'none';
                }
                if(document.querySelector(".delRvBtn")) {
                    document.querySelector(".delRvBtn").style.display = 'none';
                }
            } else {
                if(row.userId !== user.uid) {
                    if(document.querySelector(".updRvBtn")) {
                        document.querySelector(".updRvBtn").style.display = 'none';
                    }
                    if(document.querySelector(".delRvBtn")) {
                        document.querySelector(".delRvBtn").style.display = 'none';
                    }
                } else {
                    document.querySelector(".updRvBtn").style.display = 'block';
                    document.querySelector(".delRvBtn").style.display = 'block';
                }
            }

            document.querySelector("input[name='reviewId']").value = row.reviewId;
            document.querySelector("select[name='booking']").innerHTML =
                `<option value="${row.bookingId}">${row.eventInfo}</option>`;
            document.querySelector("textarea[name='rvContent']").value = row.content;

            rvDtlModal.show();
        }
    });

    // resize
    window.addEventListener('resize', function(e) {
        grid.refreshLayout();
    });

    return grid;
}

const init = () => {

    // grid 초기 세팅
    const rvListGrid = initGrid();
    getRvList();

    // 검색
    document.querySelector("button[name='searchButton']").addEventListener("click", function(e) {
        getRvList();
    });

    // 후기 등록
    document.querySelector(".registReview")?.addEventListener("click", function (e) {
        // 후기 작성 안 된 관람공연 중 선택
        getSeenEvent().then(data => {
            const selectElement = document.querySelector("select[name='seenEvent']");
            selectElement.innerHTML = `<option value="">전체</option>`;

            for(const booking of data) {
                const optionElement = document.createElement("option");
                optionElement.value = booking.bookingId;
                optionElement.textContent = booking.eventInfo;

                selectElement.appendChild(optionElement);
            }
            regRvModal.show();
        }).catch(e => {
           alert("관람 공연 정보를 가져오는데 실패했습니다.");
        });
    });

    // 등록하기
    document.querySelector(".registBtn").addEventListener("click", function (e) {
       if(document.querySelector("select[name='seenEvent']").value === "") {
           alert("관람 공연 선택은 필수입니다.");
           return;
       }
       if(document.querySelector("textarea[name='content']").value.trim() === "") {
           alert("내용을 작성해주세요.");
           return;
       }

        registReview().then(res => {
            alert("후기 등록에 성공했습니다.");
            getRvList();  // 목록 재조회
            document.querySelector("textarea[name='content']").value = "";
            regRvModal.hide();
        }).catch(e => {
           alert("후기 등록에 실패했습니다.");
        });
    });

    // 수정하기
    document.querySelector(".updRvBtn")?.addEventListener("click", function (e) {
        if (e.target.value === '수정하기') {
            document.querySelector("textarea[name='rvContent']").disabled = false;
            e.target.value = '수정 완료';

        } else {
            if(document.querySelector("textarea[name='rvContent']").value.trim() === "") {
                alert("내용을 작성해주세요.");
                return;
            }

            if(confirm("후기를 수정하시겠습니까?")) {
                updateReview().then(res => {
                    alert("후기 수정에 성공했습니다.");
                    getRvList();  // 목록 재조회
                    rvDtlModal.hide();
                    document.querySelector("textarea[name='rvContent']").disabled = true;
                    e.target.value = '수정하기';
                }).catch(e => {
                    alert("후기 수정에 실패했습니다.");
                });
            }
        }
    });

    // 삭제하기
    document.querySelector(".delRvBtn")?.addEventListener("click", function (e) {
        if(confirm("정말로 삭제하시겠습니까?")) {
            deleteReview().then(res => {
                alert("후기 삭제에 성공했습니다.");
                getRvList();  // 목록 재조회
                rvDtlModal.hide();
            }).catch(e => {
                alert("후기 삭제에 실패했습니다.");
            });
        }
    });



    // 후기목록 조회
    async function getRvList() {
        const data = {
            srhEvent: document.querySelector("input[name='srhEvent']").value,
            srhUserId: document.querySelector("input[name='srhUserId']").value,
        };

        await axios({
            method : 'post',
            url : '/review/getRvList',
            data : data,
            headers : {
                'Content-Type' : 'application/json'
            }
        }).then(res => {
            rvListGrid.resetData(res.data);
        }).catch(e => {
            alert("후기 목록을 가져오는데 실패했습니다.");
        });
    }

    // 관람공연(예매목록) 조회
    async function getSeenEvent() {
        const res = await axios({
            method : 'post',
            url : '/review/getSeenEvent',
            data : { userId: user.uid },  // loginInfo
            headers : {
                'Content-Type' : 'application/json'
            }
        });
        return res.data;
    }

    // 후기 등록
    async function registReview() {
        const data = {
            userId: user.uid,  // loginInfo
            bookingId: document.querySelector("select[name='seenEvent']").value,
            content: document.querySelector("textarea[name='content']").value
        };

        const res = await axios({
            method : 'post',
            url : '/review',
            data : data,
            headers : {
                'Content-Type' : 'application/json'
            }
        });
        return res.data;
    }

    // 후기 수정
    async function updateReview() {
        const data = {
            reviewId: document.querySelector("input[name='reviewId']").value,
            content: document.querySelector("textarea[name='rvContent']").value
        };

        const res = await axios({
            method : 'put',
            url : '/review',
            data : data,
            headers : {
                'Content-Type' : 'application/json'
            }
        });
        return res.data;
    }

    // 후기 삭제
    async function deleteReview() {
        const res = await axios({
            method : 'delete',
            url : '/review',
            data : { reviewId: document.querySelector("input[name='reviewId']").value },
            headers : {
                'Content-Type' : 'application/json'
            }
        });
        return res.data;
    }
}

window.onload = () => {
    init();
}