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
                header: '이미지',
                name: 'filePath',
                align: 'center',
                formatter: (value) => {
                    if (value.value) {
                        const imageUrl = value.value;
                        console.log("이미지경로:"+imageUrl)
                        const absoluteImageUrl = `/test/uploads/${imageUrl}`;
                        return `<img src="${absoluteImageUrl}" alt="이미지" style="max-width: 100px; max-height: 25px;">`;
                    }
                    return "";
                }
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

    // 검색
    document.querySelector(".searchBtn").addEventListener("click", function(e) {
        e.preventDefault();
        e.stopPropagation();

        // 조회
        getData();
    }, false);

    // 파일 추가 버튼
    document.querySelector(".addFileBtn").addEventListener("click", function() {
        const fileInputContainer = document.querySelector("#fileInputs");
        const newFileInput = document.createElement("input");
        newFileInput.type = "file";
        newFileInput.name = "files";
        fileInputContainer.appendChild(newFileInput);
    });

    // 등록
    document.querySelector(".registBtn").addEventListener("click", function(e) {
        e.preventDefault();
        e.stopPropagation();

        regTest();
    }, false);

    // 수정
    document.querySelector(".updateBtn").addEventListener("click", function(e) {
        e.preventDefault();
        e.stopPropagation();

        updTest();
    }, false);

    // 삭제
    document.querySelector(".deleteBtn").addEventListener("click", function(e) {
        e.preventDefault();
        e.stopPropagation();

        delTest();
    }, false);

    // 목록 조회
    async function getData() {
        // validation
        const strBirth = document.querySelector("input[name='srhStrBirth']").value;
        const endBirth = document.querySelector("input[name='srhEndBirth']").value;
        if (new Date(strBirth) > new Date(endBirth)) {
            alert("시작 날짜는 종료 날짜보다 이전이어야 합니다.");
            return;
        }
        // axios
        const data = {
            srhName: document.querySelector("input[name='srhName']").value,
            srhChkType: document.querySelector("input[name='srhChkType']").checked,
            srhStrBirth: strBirth,
            srhEndBirth: endBirth,
            srhAddress: document.querySelector("select[name='srhAddress']").value
        };
        console.log(data);

        try {
            const res = await axios.post(`/test/getTestList`, data);
            console.log(res);
            testGrid.resetData(res.data); // grid에 세팅
        } catch (e) {
            console.error(e);
        }
    }

    // 등록
    async function regTest() {
        // validation 추가

        // FormData
        const formData = new FormData();
        formData.append("name", document.querySelector("input[name='name']").value);
        formData.append("chkType", document.querySelector("input[name='chkType']").checked);
        formData.append("birth", document.querySelector("input[name='birth']").value);
        formData.append("address", document.querySelector("select[name='address']").value);

        // 파일 처리
        const fileInputs = document.querySelectorAll("input[name='files']");
        fileInputs.forEach(fileInput => {
            const files = fileInput.files;
            for (let i = 0; i < files.length; i++) {
                formData.append("files", files[i]);
            }
        });

        for (let [key, value] of formData.entries()) {
            console.log(key, value);
        }

        try {
            const res = await axios.post('/test/registTest', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            console.log(res);
            getData();
        } catch (e) {
            console.error(e);
        }
    }

    // 수정
    async function updTest() {
        alert("수정 기능");
        // validation 추가
        // data 세팅 및 axios send
        // 성공 시 다시 getData();
    }

    // 삭제
    async function delTest() {
        alert("삭제 기능");
        // validation 추가
        // id만 세팅 및 axios send
        // 성공 시 다시 getData();
    }
}

window.onload = () => {
    init();
}
