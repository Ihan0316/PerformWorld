document.addEventListener("DOMContentLoaded", function () {
    //공통 유효성 검사 함수
    function validateInput(input, message){
        if(!input || input.trim() === ''){
            alert(message);
            return false;
        }
        return true;
    }

    //공지사항 등록
    document.querySelector('.registBtn').addEventListener('click', function (){
        const title = document.querySelector('#title').value;
        const content = document.querySelector('#content').value;

        if (!validateInput(title, '제목을 입력하세요') || !validateInput(content,'내용을 입력하세요')) return;

        axios.post('/notice/registNotice', { title, content })
            .then(function () {
                alert('공지사항이 등록되었습니다.');
                //공지사항 목록을 새로 고침 또는 페이지 새로 고침
                window.location.reload();
            })
            .catch(function (error) {
                const errorMessage = error.response && error.response.data ? error.response.data.message : '등록 실패';
                alert('등록 실패 ' + errorMessage);
            });
    });

    //공지사항 수정
    document.querySelector('.updateBtn').addEventListener('click', function (){
        const noticeId = document.querySelector('#updateNoticeId').value;
        const title = document.querySelector('#updateTitle').value;
        const content = document.querySelector('#updateContent').value;

        if (!validateInput(noticeId, '수정할 공지사항을 선택하세요.') ||
            !validateInput(title,'제목을 입력하세요.') ||
            !validateInput(content,'내용을 입력하세요.')) return;

        axios.post('/notice/updateNotice', { noticeId, title, content})
            .then(function (){
                alert('공지사항이 수정되었습니다.');
                window.location.reload();
            })
            .catch(function (error){
                const errorMessage = error.response && error.response.data ? error.response.data.message : '수정 실패';
                alert('수정 실패' + errorMessage);
            });
    });

    //공지사항 삭제
    document.querySelector('.deleteBtn').addEventListener('click', function (){
        const noticeId = document.querySelector('#updateNoticeId').value;

        if (!validateInput(noticeId, '삭제할 공지사항 선택하세요.')) return;

        if (confirm('삭제하시겠습니까?')){
            axios.post('/notice/deleteNotice', { noticeId })
                .then(function (){
                    alert('공지사항이 삭제되었습니다.');
                    window.location.reload();
                })
                .catch(function (error){
                    const errorMessage = error.response && error.response.data ? error.response.data.message : '삭제 실패';
                    alert('삭제 실패' + errorMessage);
                });
        }
    });
});