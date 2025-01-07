document.addEventListener("DOMContentLoaded", function () {
    // 문의사항 등록
    document.querySelector('.registBtn').addEventListener('click', function () {
        const title = document.querySelector('#title').value;
        const content = document.querySelector('#content').value;

        axios.post('/qna/registQnA', {
            title: title,
            content: content
        })
            .then(function (response) {
                alert('문의사항이 등록되었습니다.');
                window.location.reload();
            })
            .catch(function (error) {
                alert('등록 실패: ' + error);
            });
    });

    // 문의사항 답변
    document.querySelector('.responseBtn').addEventListener('click', function () {
        const qnaId = document.querySelector('#responseQnAId').value;
        const response = document.querySelector('#response').value;
        const responseDatetime = document.querySelector('#responseDatetime').value;

        if (!response || !responseDatetime) {
            alert('답변 내용과 답변일을 모두 입력해 주세요.');
            return;
        }

        axios.post('/qna/respondQnA', {
            qnaId: qnaId,
            response: response,
            responseDatetime: responseDatetime
        })
            .then(function (response) {
                alert('답변이 등록되었습니다.');
                window.location.reload();
            })
            .catch(function (error) {
                alert('답변 실패: ' + error);
            });
    });

    // 문의사항 삭제
    document.querySelector('.deleteBtn').addEventListener('click', function () {
        const qnaId = document.querySelector('#responseQnAId').value;

        if (confirm('삭제하시겠습니까?')) {
            axios.post('/qna/deleteQnA', {
                qnaId: qnaId
            })
                .then(function (response) {
                    alert('문의사항이 삭제되었습니다.');
                    window.location.reload();
                })
                .catch(function (error) {
                    alert('삭제 실패: ' + error);
                });
        }
    });
});
