// grid 초기화
const initGrid = () => {
    const Grid = tui.Grid;

    // 테마
    Grid.applyTheme('default',  {
        cell: {
            normal: {
                border: 'gray'
            },
            header: {
                background: 'gray',
                text: 'white',
                border: 'gray'
            },
            rowHeaders: {
                header: {
                    background: 'gray',
                    text: 'white'
                }
            }
        }
    });

    // 세팅
    return new Grid({
        el: document.getElementById('grid'),
        scrollX: false,
        scrollY: false,
        minBodyHeight: 30,
        rowHeaders: ['rowNum'],
        pageOptions: {
            useClient: true,  // 프론트에서 페이징
            perPage: 10
        },
        columns: [
            {
                header: '번호',
                name: 'id',
                hidden: true
            },
            {
                header: '이름',
                name: 'name',
                align: 'center'
            },
            {
                header: '체크타입',
                name: 'chkType',
                align: 'center',
                formatter: (value) => {
                    if (value) {
                        const data = value.value;
                        return `${data ? '완료' : '취소'}`;
                    }
                    return "";
                }
            },
            {
                header: '생일',
                name: 'birth',
                formatter: (value) => {
                    if (value) {
                        const data = value.value;
                        return `${data[0]}-${data[1]}-${data[2]}`;
                    }
                    return "";
                }
            },
            {
                header: '주소',
                name: 'address',
                align: 'center'
            },
            {
                header: '등록일',
                name: 'regDate',
                formatter: (value) => {
                    if (value) {
                        const data = value.value;
                        return `${data[0]}-${data[1]}-${data[2]}`;
                    }
                    return "";
                }
            }
        ]
    });
}

const init = () => {
    // grid 초기 세팅
    const testGrid = initGrid();

    // data 가져오기
    async function getData() {
        // validation
        const strBirth = document.querySelector("input[name='srhStrBirth']").value;
        const endBirth = document.querySelector("input[name='srhEndBirth']").value;
        if (new Date(strBirth) > new Date(endBirth)) {
            alert("시작 날짜는 종료 날짜보다 이전이어야 합니다.");
            return;
        }

        // send
        const srhObj = {
            srhName: document.querySelector("input[name='srhName']").value,
            srhChkType: document.querySelector("input[name='srhChkType']").checked,
            srhStrBirth: strBirth,
            srhEndBirth: endBirth,
            srhAddress: document.querySelector("select[name='srhAddress']").value
        };
        console.log(srhObj);

        try {
            const res = await axios.post(`/test/getTestList`, srhObj);
            console.log(res);
            // grid에 세팅
            testGrid.resetData(res.data);
        } catch (e) {
            console.error(e);
        }
    }

     // 검색
     document.querySelector(".searchBtn").addEventListener("click", function(e) {
        e.preventDefault();
        e.stopPropagation();

        getData();
     }, false);
}

window.onload = () => {
    init();
}
