document.querySelectorAll('.qna-question').forEach((button) => {
    button.addEventListener('click', () => {
        const answer = button.nextElementSibling;

        // 이미 펼쳐져 있을 때는 숨기기
        if (answer.style.maxHeight && answer.style.maxHeight !== '0px') {
            answer.style.maxHeight = '0px';
            answer.classList.remove('open');  // padding 0으로 되돌리기
        } else {
            // 숨겨져 있을 때는 max-height를 auto로 설정하여 내용이 자연스럽게 펼쳐지게 함
            answer.style.maxHeight = '1000px';  // 충분히 큰 값으로 설정하여 내용이 잘리지 않게
            answer.classList.add('open');  // padding 원래대로 복원
        }
    });
});

const modal = new bootstrap.Modal(document.querySelector('.boardRegisterModal'));
document.querySelector('.regBtn').addEventListener('click', function () {
    modal.show(); // 모달을 보이게 함
});

document.getElementById('saveBoard').addEventListener('click', async function () {
    const question = document.getElementById('question').value;
    const answer = document.getElementById('answer').value;

    // 유효성 검사 (예: 질문과 답변이 비어있지 않도록)
    if (!question || !answer) {
        alert('질문과 답변을 모두 입력해주세요.');
        return;
    }

    try {
        // 타입에 맞는 URL로 요청을 보냄
        const url = modalType === 'faq' ? '/board/faqSave' : '/board/noticeSave';
        const payload = modalType === 'faq'
            ? { question, answer }  // FAQ에 저장할 데이터
            : { title: question, content: answer };  // 공지사항에 저장할 데이터

        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(payload),
        });

        // 응답 확인
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // 저장이 완료되면 모달을 닫고 데이터 갱신
        alert('저장이 완료되었습니다.');
        document.querySelector('.closeBtn').click();  // 모달 닫기
        modalType === 'faq' ? loadFAQs() : loadNotices();  // 새 데이터를 불러오기
    } catch (error) {
        console.error('저장 오류:', error);
        alert('저장 중 오류가 발생했습니다.');
    }
});


document.querySelector('.faqBtn').addEventListener('click', function () {
    // FAQ 데이터를 새로 불러오기
    document.querySelector('.faq-header h3').textContent = '자주하는 질문';
    modalType = 'faq';
    loadFAQs();
});

document.querySelector('.noticeBtn').addEventListener('click', function () {
    // FAQ 데이터를 새로 불러오기
    document.querySelector('.faq-header h3').textContent = '공지 사항';
    modalType = 'notice';
    loadNotices();
});

let modalType = 'faq';

document.querySelector('.qnaBtn').addEventListener('click', async function () {
    try {
        // 서버에서 Q&A 데이터를 가져오는 요청
        const response = await fetch('/board/getQnAList');

        // 응답이 성공적이지 않으면 예외 발생
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // 응답에서 JSON 데이터를 가져옴
        const qnaData = await response.json();

        // 기존 FAQ 데이터를 교체
        const faqBoard = document.getElementById('faq-board');
        faqBoard.innerHTML = ''; // 기존 내용 삭제

        qnaData.forEach(qna => {
            // Q&A 항목을 div로 생성
            const qnaItem = document.createElement('div');
            qnaItem.classList.add('qna-item');

            // 질문 버튼 생성
            const questionButton = document.createElement('button');
            questionButton.classList.add('qna-question');
            questionButton.textContent = qna.question;

            // 답변 div 생성
            const answerDiv = document.createElement('div');
            answerDiv.classList.add('qna-answer');
            answerDiv.textContent = qna.answer;

            // Q&A 항목에 질문과 답변 추가
            qnaItem.appendChild(questionButton);
            qnaItem.appendChild(answerDiv);

            // qna-board에 항목 추가
            faqBoard.appendChild(qnaItem);

            // 답변 펼치기 기능 추가
            questionButton.addEventListener('click', () => {
                const answer = questionButton.nextElementSibling;

                if (answer.style.maxHeight && answer.style.maxHeight !== '0px') {
                    answer.style.maxHeight = '0px';
                    answer.classList.remove('open');
                } else {
                    answer.style.maxHeight = '1000px';
                    answer.classList.add('open');
                }
            });
        });
    } catch (error) {
        console.error('Q&A 로딩 오류:', error);
    }
});

async function loadNotices() {
    try {
        const response = await fetch('/board/getNoticeList');

        // 응답이 성공적인지 확인
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // 응답을 텍스트로 받는다 (XML 형식)
        const xmlText = await response.text();
        console.log("공지사항 응답 내용:", xmlText);  // 응답 내용을 로그로 출력

        // XML 파싱
        const parser = new DOMParser();
        const xmlDoc = parser.parseFromString(xmlText, "application/xml");

        // XML에서 item을 찾는다
        const items = xmlDoc.getElementsByTagName("item");

        const notices = Array.from(items).map(item => {
            return {
                noticeId: item.getElementsByTagName("noticeId")[0]?.textContent,
                title: item.getElementsByTagName("title")[0]?.textContent,
                content: item.getElementsByTagName("content")[0]?.textContent,
            };
        });

        const faqBoard = document.getElementById('faq-board');
        faqBoard.innerHTML = ''; // 기존 내용을 비운다

        notices.forEach(notice => {
            const noticeItem = document.createElement('div');
            noticeItem.classList.add('qna-item');

            // 제목 버튼 생성
            const titleButton = document.createElement('button');
            titleButton.classList.add('qna-question');
            titleButton.textContent = notice.title;

            // 내용 div 생성
            const contentDiv = document.createElement('div');
            contentDiv.classList.add('qna-answer');
            contentDiv.textContent = notice.content;  // 텍스트만 넣음
            contentDiv.innerHTML = notice.content.replace(/\n/g, '<br>');  // 줄바꿈 처리

            titleButton.addEventListener('click', () => {
                const content = titleButton.nextElementSibling;

                // 이미 펼쳐져 있을 때는 숨기기
                if (content.style.maxHeight && content.style.maxHeight !== '0px') {
                    content.style.maxHeight = '0px';
                    content.classList.remove('open');  // padding 0으로 되돌리기
                } else {
                    // 숨겨져 있을 때는 max-height를 auto로 설정하여 내용이 자연스럽게 펼쳐지게 함
                    content.style.maxHeight = '1000px';  // 충분히 큰 값으로 설정하여 내용이 잘리지 않게
                    content.classList.add('open');  // padding 원래대로 복원
                }
            });

            // 공지사항 항목에 제목과 내용 추가
            noticeItem.appendChild(titleButton);
            noticeItem.appendChild(contentDiv);

            // faqBoard에 항목 추가
            faqBoard.appendChild(noticeItem);
        });
    } catch (error) {
        console.error('공지사항 로딩 오류:', error);
    }
}

async function loadFAQs() {
    try {
        const response = await fetch('/board/getFAQList');

        // 응답이 성공적인지 확인
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // 응답을 텍스트로 받는다 (XML 형식)
        const xmlText = await response.text();

        // XML 파싱
        const parser = new DOMParser();
        const xmlDoc = parser.parseFromString(xmlText, "application/xml");

        // XML에서 item을 찾는다
        const items = xmlDoc.getElementsByTagName("item");

        const faqs = Array.from(items).map(item => {
            return {
                faqId: item.getElementsByTagName("faqId")[0]?.textContent,
                question: item.getElementsByTagName("question")[0]?.textContent,
                answer: item.getElementsByTagName("answer")[0]?.textContent,
            };
        });

        const faqBoard = document.getElementById('faq-board');
        faqBoard.innerHTML = ''; // 기존 내용을 비운다

        faqs.forEach(faq => {
            const faqItem = document.createElement('div');
            faqItem.classList.add('qna-item');

            // 질문 버튼 생성
            const questionButton = document.createElement('button');
            questionButton.classList.add('qna-question');
            questionButton.textContent = faq.question;

            // 답변 div 생성
            const answerDiv = document.createElement('div');
            answerDiv.classList.add('qna-answer');
            answerDiv.textContent = faq.answer;  // 텍스트만 넣음
            answerDiv.innerHTML = faq.answer.replace(/\n/g, '<br>');

            questionButton.addEventListener('click', () => {
                const answer = questionButton.nextElementSibling;

                // 이미 펼쳐져 있을 때는 숨기기
                if (answer.style.maxHeight && answer.style.maxHeight !== '0px') {
                    answer.style.maxHeight = '0px';
                    answer.classList.remove('open');  // padding 0으로 되돌리기
                } else {
                    // 숨겨져 있을 때는 max-height를 auto로 설정하여 내용이 자연스럽게 펼쳐지게 함
                    answer.style.maxHeight = '1000px';  // 충분히 큰 값으로 설정하여 내용이 잘리지 않게
                    answer.classList.add('open');  // padding 원래대로 복원
                }
            });

            // FAQ 항목에 질문과 답변 추가
            faqItem.appendChild(questionButton);
            faqItem.appendChild(answerDiv);

            // faqBoard에 항목 추가
            faqBoard.appendChild(faqItem);
        });
    } catch (error) {
        console.error('FAQ 로딩 오류:', error);
    }
}



// 페이지 로드 시 FAQ 데이터 가져오기
window.onload = loadFAQs;