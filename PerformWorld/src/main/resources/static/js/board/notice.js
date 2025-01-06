document.addEventListener("DOMContentLoaded", function () {
    //공지사항 등록
    document.querySelector('.registBtn').addEventListener('click', function () {
        const title = document.querySelector('#title').value;
        const content = document.querySelector('#content').value;

        axios.post('/notice/registNotice', {
            title: title,
            content: content
        })
            .then(function (response){
                alert('공지사항이 등록되었습니다.');
                window.location.reload();
            })
            .catch(function (error) {
                alert('등록 실패: ' + error);
            });
    });

    //공지사항 수정
    document.querySelector('.updateBtn').addEventListener('click', function () {
        const noticeId = document.querySelector('#updateNoticeId').value;
        const title = document.querySelector('#updateTitle').value;
        const content = document.querySelector('#updateContent').value;

        axios.post('/notice/updateNotice', {
            noticeId: noticeId,
            title: title,
            content: content
        })
            .then(function (response) {
                alert('공지사항이 수정되었습니다.');
                window.location.reload();
            })
            .catch(function (error) {
                alert('수정 실패: ' + error);
            });
    });

    //공지사항 삭제
    document.querySelector('.deleteBtn').addEventListener('click', function () {
        const noticeId = document.querySelector('#updateNoticeId').value;

        if (confirm('삭제하시겠습니까?')) {
            axios.post('/notice/deleteNotice', {
                noticeId: noticeId
            })
                .then(function (response) {
                    alert('공지사항이 삭제되었습니다.');
                    window.location.reload();
                })
                .catch(function (error) {
                    alert('삭제 실패: ' + error);
                });
        }
    });
});