document.addEventListener("DOMContentLoaded", function () {
    // 공통 유효성 검사 함수
    function validateInput(input, message) {
        if (!input || input.trim() === '') {
            alert(message);
            return false;
        }
        return true;
    }

    // FAQ 등록
    document.querySelector('.registBtn').addEventListener('click', function () {
        const question = document.querySelector('#question').value;
        const answer = document.querySelector('#answer').value;

        // 유효성 검사
        if (!validateInput(question, '질문을 입력하세요.') || !validateInput(answer, '답변을 입력하세요.')) return;

        axios.post('/faq/registFAQ', { question, answer })
            .then(function () {
                alert('FAQ가 등록되었습니다.');
                window.location.reload();
            })
            .catch(function (error) {
                alert('등록 실패: ' + error.message);
            });
    });

    // FAQ 수정
    const updateButtons = document.querySelectorAll('.updateBtn');
    updateButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            const faqId = this.getAttribute('data-id');

            // 기존 데이터를 폼에 채우기
            const question = document.querySelector(`#question_${faqId}`).textContent;
            const answer = document.querySelector(`#answer_${faqId}`).textContent;

            const updateForm = document.querySelector('#faq-regist-form');
            updateForm.style.display = 'block'; // 폼 표시
            document.querySelector('#question').value = question;
            document.querySelector('#answer').value = answer;

            // 기존 등록 이벤트와 충돌 방지
            const saveButton = document.querySelector('.registBtn');
            saveButton.removeEventListener('click', saveButton._updateHandler);

            const updateHandler = function () {
                const updatedQuestion = document.querySelector('#question').value;
                const updatedAnswer = document.querySelector('#answer').value;

                if (!validateInput(updatedQuestion, '수정할 질문을 입력하세요.') || !validateInput(updatedAnswer, '수정할 답변을 입력하세요.')) return;

                axios.post('/faq/updateFAQ', {
                    faqId,
                    question: updatedQuestion,
                    answer: updatedAnswer
                })
                    .then(function () {
                        alert('FAQ가 수정되었습니다.');
                        window.location.reload();
                    })
                    .catch(function (error) {
                        alert('수정 실패: ' + error.message);
                    });
            };

            saveButton._updateHandler = updateHandler; // 핸들러 참조 저장
            saveButton.addEventListener('click', updateHandler);
        });
    });

    // FAQ 삭제
    const deleteButtons = document.querySelectorAll('.deleteBtn');
    deleteButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            const faqId = this.getAttribute('data-id');

            if (confirm('삭제하시겠습니까?')) {
                axios.post('/faq/deleteFAQ', { faqId })
                    .then(function () {
                        alert('FAQ가 삭제되었습니다.');
                        window.location.reload();
                    })
                    .catch(function (error) {
                        alert('삭제 실패: ' + error.message);
                    });
            }
        });
    });
});

