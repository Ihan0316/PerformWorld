document.addEventListener("DOMContentLoaded", function (){
    //문의사항 등록(회원용)
    document.querySelector(',registQnaBtn').addEventListener('click', function (){
        const title = document.querySelector('#qnaTitle').value;
        const content = document.querySelector('#qnaContent').value;

        axios.post('/qna/registQna', {
            title: title,
            content: content
        })
            .then(function (response){
                alert('문의가 등록되었습니다.');
                window.location.reload();
            })
            .catch(function (error){
                alert('등록 실패: ' + error);
            });
    });

    //답변 수정(관리자용)
    document.querySelector('.responseQnaBtn').addEventListener('click', function (){
        const qnaId = document.querySelector('#responseQnaId').value;
        const response = document.querySelector('#qnaResponse').value;

        axios.post('/qna/respondQna', {
            qnaId: qnaId,
            response: response
        })
            .then(function (response){
                alert('답변이 수정되었습니다.');
                window.location.reload();
            })
            .catch(function (error){
                alert('수정 실패: ' + error);
            });
    });

    //문의 삭제(관리자용)
    document.querySelector('.deleteQnaBtn').addEventListener('click', function () {
        const qnaId = document.querySelector('#deleteQnaId').value;

        if (confirm('삭제하시겠습니까?')) {
            axios.post('qna/deleteQna', {
                qnaId: qnaId
            })
                .then(function (response){
                    alert('문의가 삭제되었습니다.');
                    window.location.reload();
                })
                .catch(function (error){
                    alert('삭제 실패: ' + error);
                });
        }
    });
});
