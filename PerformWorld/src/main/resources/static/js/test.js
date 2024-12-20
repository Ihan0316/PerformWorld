// grid 초기화
const initGrid = () => {
    const Grid = tui.Grid;

    // 테마
    Grid.applyTheme('default',  {
        cell: {
            normal: {
                border: 'black'
            },
            header: {
                background: 'gray',
                text: 'white'
            },
            // evenRow: {
            //     background: '#eee'
            // }
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
                name: 'fno',
                hidden: true
            },
            {
                header: '제목',
                name: 'title'
            },
            {
                header: '내용',
                name: 'content',
                editor: 'text'
            },
            {
                header: '작성자',
                name: 'writer'
            },
            {
                header: '등록일',
                name: 'regDate',
            }
        ]
    });
}

// data 가져오기
// async function setData() {
//     return await axios.post(`/test/getList`);
// }

window.onload = () => {
    const testGrid = initGrid();
    testGrid.resetData({});

    // setData().then(res => {
    //     console.log(res);
    //     // grid 그리기
    //     const testGrid = initGrid();
    //     testGrid.resetData(res.data);
    //
    // }).catch(e => {
    //     console.error(e);
    // });
}
