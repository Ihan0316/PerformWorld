// grid 초기화
const initNoticeGrid = () => {
    const NoticeGrid = tui.Grid;

    // 테마
    NoticeGrid.applyTheme('clean', {
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

    const noticeGridEl = document.getElementById('noticeGrid');
    noticeGridEl.innerHTML = ''; // 기존 그리드 초기화

    // 세팅
    const noticeGrid = new NoticeGrid({
        el: noticeGridEl,
        scrollX: false,
        scrollY: false,
        minBodyHeight: 150,
        pageOptions: {
            useClient: true,  // 프론트에서 페이징
            perPage: 10
        },
        columns: [
            {
                header: '번호',
                name: 'noticeId',
                hidden: true
            },
            {
                header: '제목',
                name: 'title',
                align: 'center',
                width: 350
            },
            {
                header: '내용',
                name: 'content',
                minWidth: 200
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
    noticeGrid.on('dblclick', (e) => {
        const rowKey = e.rowKey;  // 클릭한 행의 rowKey
        if (rowKey !== null) {
            console.log('클릭된 행:', noticeGrid.getRow(rowKey));
            const row = noticeGrid.getRow(rowKey);

            document.querySelector(".noticeDtlModal input[name='noticeId']").value = row.noticeId;
            document.querySelector(".noticeDtlModal input[name='title']").value = row.title;
            document.querySelector(".noticeDtlModal textarea[name='content']").value = row.content;
            noticeDtlModal.show();
        }
    });

    // resize
    window.addEventListener('resize', function (e) {
        noticeGrid.refreshLayout();
    });

    return noticeGrid;
}

const initQnaGrid = () => {
    const QnaGrid = tui.Grid;

    // 테마
    QnaGrid.applyTheme('clean', {
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

    const qnaGridEl = document.getElementById('qnaGrid');
    qnaGridEl.innerHTML = ''; // 기존 그리드 초기화

    // 세팅
    const qnaGrid = new QnaGrid({
        el: qnaGridEl,
        scrollX: false,
        scrollY: false,
        minBodyHeight: 150,
        pageOptions: {
            useClient: true,  // 프론트에서 페이징
            perPage: 5
        },
        columns: [
            {
                header: '번호',
                name: 'qnaId',
                hidden: true
            },
            {
                header: '문의 제목',
                name: 'title'
            },
            {
                header: '작성자',
                name: 'userId',
                align: 'center',
                width: 100
            },
            {
                header: '답변 여부',
                name: 'response',
                align: 'center',
                width: 100,
                formatter: (value) => {
                    if (value.value) {
                        return "완료";
                    }
                    return "";
                }
            }
        ]
    });

    // 행 더블클릭 시 상세 정보로
    qnaGrid.on('dblclick', (e) => {
        const rowKey = e.rowKey;  // 클릭한 행의 rowKey
        if (rowKey !== null) {
            console.log('클릭된 행:', qnaGrid.getRow(rowKey));
            const row = qnaGrid.getRow(rowKey);

            document.querySelector(".qnaDtlModal input[name='qnaId']").value = row.qnaId;
            document.querySelector(".qnaDtlModal input[name='title']").value = row.title;
            document.querySelector(".qnaDtlModal input[name='userId']").value = row.userId;
            document.querySelector(".qnaDtlModal textarea[name='content']").value = row.content;
            document.querySelector(".qnaDtlModal textarea[name='response']").value = row.response;
            qnaDtlModal.show();
        }
    });

    // resize
    window.addEventListener('resize', function (e) {
        qnaGrid.refreshLayout();
    });

    return qnaGrid;
}

const init = () => {
    let modalType = 'notice';

    // grid 초기 세팅
    let noticeGrid = initNoticeGrid();
    let qnaGrid = initQnaGrid();
    loadNotices().then(res => {
        if (res !== "") {
            noticeGrid.resetData(res);  // grid에 세팅
        }
    }).catch(e => {
        alert("Notice 목록을 가져오는데 실패했습니다.");
    });

    // 공지사항 탭
    document.getElementById("notice-tab").addEventListener("click", function () {
        setActiveTab("notice-tab", "notice-board");
        modalType = 'notice';
        noticeGrid = initNoticeGrid();
        loadNotices().then(res => {
            if (res !== "") {
                noticeGrid.resetData(res);  // grid에 세팅
            }
        }).catch(e => {
            alert("QnA 목록을 가져오는데 실패했습니다.");
        });
    });

    // QnA 탭
    document.getElementById("qna-tab").addEventListener("click", function () {
        setActiveTab("qna-tab", "qna-board");
        modalType = 'qna';
        qnaGrid = initQnaGrid();
        loadQnas().then(res => {
            if (res !== "") {
                qnaGrid.resetData(res);  // grid에 세팅
            }
        }).catch(e => {
            alert("QnA 목록을 가져오는데 실패했습니다.");
        });
    });

    // FAQ 탭
    document.getElementById("faq-tab").addEventListener("click", function () {
        setActiveTab("faq-tab", "faq-board");
        modalType = 'faq';
        loadFAQs();
    });

    // faq 질문 클릭시 답변 펼치기
    document.querySelectorAll('.faq-question').forEach((button) => {
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

    // 등록 버튼 클릭 시 tab에 따라 다른 모달 오픈
    document.querySelectorAll('.regBtn').forEach(btn => {
        btn.addEventListener('click', function () {
            if (modalType === 'notice') {
                noticeRegModal.show();
            }
            if (modalType === 'qna') {
                document.querySelector(".qnaRegModal input[name='userId']").value;  // loginInfo
                qnaRegModal.show();
            }
            if (modalType === 'faq') {
                faqRegModal.show();
            }
        });
    });

    // tab mode 변경
    function setActiveTab(tabId, contentId) {
        // active 클래스 제거
        let tabs = document.querySelectorAll(".tab");
        tabs.forEach(function (tab) {
            tab.classList.remove("active");
        });
        let contents = document.querySelectorAll(".board");
        contents.forEach(function (content) {
            content.classList.remove("active");
        });
        // 클릭한 탭 및 콘텐츠에 active 클래스 추가
        document.getElementById(tabId).classList.add("active");
        document.getElementById(contentId).classList.add("active");
    }

    // 공지사항 목록 조회
    async function loadNotices() {
        const res = await axios({
            method: 'post',
            url: '/board/getNoticeList',
            data: {title: ''},
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return res.data;
    }

    // qna 목록 조회
    async function loadQnas() {
        const res = await axios({
            method: 'post',
            url: '/board/getQnAList',
            data: {userId: ''},
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return res.data;
    }

    // faq 목록 조회 후 세팅
    async function loadFAQs() {
        await axios({
            method: 'post',
            url: '/board/getFAQList',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            const faqs = res.data;
            if (faqs !== "") {
                const faqBoard = document.getElementById('faq-board');
                faqBoard.innerHTML = ''; // 기존 내용을 비운다

                // 등록 버튼 생성
                const saveButton = document.createElement('button');
                saveButton.classList.add('btn');
                saveButton.classList.add('btn-primary');
                saveButton.classList.add('btn-sm');
                saveButton.classList.add('regBtn');
                saveButton.textContent = 'FAQ 등록';
                saveButton.onclick = () => {
                    faqRegModal.show();
                };

                faqBoard.appendChild(saveButton);

                faqs.forEach(faq => {
                    const faqItem = document.createElement('div');
                    faqItem.classList.add('faq-item');

                    // 질문 버튼 생성
                    const questionButton = document.createElement('button');
                    questionButton.classList.add('faq-question');
                    questionButton.textContent = `Q. ${faq.question}`;

                    // 답변 div 생성
                    const answerDiv = document.createElement('div');
                    answerDiv.classList.add('faq-answer');
                    answerDiv.textContent = faq.answer;  // 텍스트만 넣음
                    answerDiv.innerHTML = faq.answer.replace(/\n/g, '<br>');
                    answerDiv.onclick = () => {
                        document.querySelector(".faqDtlModal input[name='faqId']").value = faq.faqId;
                        document.querySelector(".faqDtlModal input[name='question']").value = faq.question;
                        document.querySelector(".faqDtlModal textarea[name='answer']").value = faq.answer;
                        faqDtlModal.show();
                    };

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
            }

        }).catch(e => {
            alert("FAQ 목록을 가져오는데 실패했습니다.");
        });
    }

    // 등록하기
    document.querySelectorAll("button[name='saveBoard']").forEach(regBtn => {
        regBtn.addEventListener('click', async function () {
            // notice 탭일 경우
            if (modalType === 'notice') {
                const title = document.querySelector(".noticeRegModal input[name='title']").value.trim();
                const content = document.querySelector(".noticeRegModal textarea[name='content']").value.trim();
                if (!title || !content) {
                    alert('제목과 내용을 모두 입력해주세요.');
                    return;
                }

                const data = {title: title, content: content};
                await axios({
                    method: 'post',
                    url: '/board/noticeSave',
                    data: data,
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(res => {
                    alert("공지사항 등록에 성공했습니다.");
                    loadNotices().then(res => {
                        if (res !== "") {
                            noticeGrid.resetData(res);  // grid에 세팅
                        }
                        noticeRegModal.hide();

                        // 모달 입력 필드 초기화
                        document.querySelector(".noticeRegModal input[name='title']").value = '';
                        document.querySelector(".noticeRegModal textarea[name='content']").value = '';
                    }).catch(e => {
                        alert("Notice 목록을 가져오는데 실패했습니다.");
                    });

                }).catch(e => {
                    alert("공지사항 등록에 실패했습니다.");
                });
            }
            // qna 탭일 경우
            if (modalType === 'qna') {
                const title = document.querySelector(".qnaRegModal input[name='title']").value.trim();
                const content = document.querySelector(".qnaRegModal textarea[name='content']").value.trim();
                if (!title || !content) {
                    alert('제목과 내용을 모두 입력해주세요.');
                    return;
                }

                const data = {
                    title: title,
                    content: content,
                    userId: 'user123'  // loginInfo
                };
                await axios({
                    method: 'post',
                    url: '/board/qnaSave',
                    data: data,
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(res => {
                    alert("Q&A 등록에 성공했습니다.");
                    loadQnas().then(res => {
                        if (res !== "") {
                            qnaGrid.resetData(res);  // grid에 세팅
                        }
                        qnaRegModal.hide();

                        // 모달 입력 필드 초기화
                        document.querySelector(".qnaRegModal input[name='title']").value = '';
                        document.querySelector(".qnaRegModal textarea[name='content']").value = '';
                    }).catch(e => {
                        alert("QnA 목록을 가져오는데 실패했습니다.");
                    });

                }).catch(e => {
                    alert("Q&A 등록에 실패했습니다.");
                });
            }
            // faq 탭일 경우
            if (modalType === 'faq') {
                const question = document.querySelector(".faqRegModal input[name='question']").value.trim();
                const answer = document.querySelector(".faqRegModal textarea[name='answer']").value.trim();
                if (!question || !answer) {
                    alert('질문과 답변을 모두 입력해주세요.');
                    return;
                }

                const data = {question: question, answer: answer};
                await axios({
                    method: 'post',
                    url: '/board/faqSave',
                    data: data,
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(res => {
                    alert("FAQ 등록에 성공했습니다.");
                    loadFAQs();
                    faqRegModal.hide();

                    // 모달 입력 필드 초기화
                    document.querySelector(".faqRegModal input[name='question']").value = '';
                    document.querySelector(".faqRegModal textarea[name='answer']").value = '';
                }).catch(e => {
                    alert("FAQ 등록에 실패했습니다.");
                });
            }
        });
    });

    // 삭제하기
    document.querySelectorAll("button[name='deleteBoard']").forEach(delBtn => {
        delBtn.addEventListener('click', async function () {
            // notice 탭일 경우
            if (modalType === 'notice') {
                await axios({
                    method: 'delete',
                    url: '/board/notice',
                    data: {noticeId: document.querySelector("input[name='noticeId']").value},
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(res => {
                    alert("공지사항 삭제에 성공했습니다.");
                    loadNotices().then(res => {
                        if (res !== "") {
                            noticeGrid.resetData(res);  // grid에 세팅
                        }
                        noticeDtlModal.hide();
                    }).catch(e => {
                        alert("공지사항 삭제에 실패했습니다.");
                    });

                }).catch(e => {
                    alert("공지사항 삭제에 실패했습니다.");
                });
            }
            // qna 탭일 경우
            if (modalType === 'qna') {
                const modal = document.querySelector(".qnaDtlModal");
                const contentuserId = modal.querySelector("input[name='userId']").value;
                const userID = document.getElementById('username').value;
                if (contentuserId !== userID) {
                    alert("Q&A를 삭제할 수 없는 회원이에요.");
                    return;
                }
                await axios({
                    method: 'delete',
                    url: '/board/qna',
                    data: {qnaId: document.querySelector("input[name='qnaId']").value},
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(res => {
                    alert("Q&A 삭제에 성공했습니다.");
                    loadQnas().then(res => {
                        if (res !== "") {
                            qnaGrid.resetData(res);  // grid에 세팅
                        }
                        qnaDtlModal.hide();
                    }).catch(e => {
                        alert("Q&A 삭제에 실패했습니다.");
                    });

                }).catch(e => {
                    alert("Q&A 삭제에 실패했습니다.");
                });
            }
            // faq 탭일 경우
            if (modalType === 'faq') {
                await axios({
                    method: 'delete',
                    url: '/board/faq',
                    data: {faqId: document.querySelector("input[name='faqId']").value},
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(res => {
                    alert("FAQ 삭제에 성공했습니다.");
                    loadFAQs();
                    faqDtlModal.hide();
                }).catch(e => {
                    alert("FAQ 삭제에 실패했습니다.");
                });
            }
        });
    });

    // 수정하기
    document.querySelectorAll("button[name='updateBoard']").forEach(updateBtn => {
        updateBtn.addEventListener('click', async function () {
            // notice 탭일 경우
            if (modalType === 'notice') {
                const modal = document.querySelector(".noticeDtlModal");
                const titleInput = modal.querySelector("input[name='title']");
                const contentTextarea = modal.querySelector("textarea[name='content']");
                if (updateBtn.textContent === '수정') {
                    // 입력 필드를 수정 가능하도록 활성화
                    titleInput.disabled = false;
                    contentTextarea.disabled = false;
                    updateBtn.textContent = '저장';  // 버튼 텍스트를 '저장'으로 변경
                } else {
                    // 입력값 검증
                    const title = titleInput.value.trim();
                    const content = contentTextarea.value.trim();

                    if (!title || !content) {
                        alert("제목과 내용을 모두 입력해주세요.");
                        return;
                    }
                    // 제목과 내용 값을 가져와 수정된 데이터로 서버에 전송
                    const updatedNoticeData = {
                        noticeId: modal.querySelector("input[name='noticeId']").value,
                        title: titleInput.value,
                        content: contentTextarea.value
                    };

                    await axios({
                        method: 'put',
                        url: '/board/noticeUpdate',
                        data: updatedNoticeData,
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    }).then(res => {
                        alert("공지사항 수정에 성공했습니다.");
                        loadNotices().then(res => {
                            if (res !== "") {
                                noticeGrid.resetData(res);  // grid에 세팅
                            }
                            // 수정 후 버튼을 다시 '수정'으로 변경하고 입력창을 비활성화
                            titleInput.disabled = true;
                            contentTextarea.disabled = true;
                            updateBtn.textContent = '수정';
                            noticeDtlModal.hide();

                        }).catch(e => {
                            alert("공지사항 수정에 실패했습니다.");
                        });
                    }).catch(e => {
                        alert("공지사항 수정에 실패했습니다.");
                    });
                }
            }
            // qna 탭일 경우
            if (modalType === 'qna') {
                const modal = document.querySelector(".qnaDtlModal");
                const titleInput = modal.querySelector("input[name='title']");
                const contentTextarea = modal.querySelector("textarea[name='content']");
                const ResponseArea = modal.querySelector("textarea[name='response']");
                const contentuserId = modal.querySelector("input[name='userId']").value;
                const userID = document.getElementById('username').value;
                if (contentuserId !== userID) {
                    alert("Q&A를 수정할 수 없는 회원이에요.");
                    return;
                }
                if (ResponseArea.value.trim() ==="") {
                    if (updateBtn.textContent === '수정') {
                        // 입력 필드를 수정 가능하도록 활성화
                        titleInput.disabled = false;
                        contentTextarea.disabled = false;
                        updateBtn.textContent = '저장';  // 버튼 텍스트를 '저장'으로 변경
                    } else {
                        // 입력값 검증
                        const title = titleInput.value.trim();
                        const content = contentTextarea.value.trim();

                        if (!title || !content) {
                            alert('제목과 내용을 모두 입력해주세요.');
                            return;
                        }
                        // 제목과 내용 값을 가져와 수정된 데이터로 서버에 전송
                        const updatedQnAData = {
                            qnaId: modal.querySelector("input[name='qnaId']").value,
                            userId: modal.querySelector("input[name='userId']").value,
                            title: titleInput.value,
                            content: contentTextarea.value,
                            response: modal.querySelector("textarea[name='response']").value
                        };
                        await axios({
                            method: 'put',
                            url: '/board/qnaUpdate',
                            data: updatedQnAData,
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }).then(res => {
                            alert("Q&A 수정에 성공했습니다.");
                            titleInput.disabled = true;
                            contentTextarea.disabled = true;
                            updateBtn.textContent = '수정';
                            qnaDtlModal.hide();
                            loadQnas().then(res => {
                                if (res !== "") {
                                    qnaGrid.resetData(res);  // grid에 세팅
                                }
                            }).catch(e => {
                                alert("Q&A 수정에 실패했습니다.");
                            });

                        }).catch(e => {
                            alert("Q&A 수정에 실패했습니다.");
                        });
                    }
                }else{
                    alert("답변 등록 후에는 수정이 불가능합니다.")
                }
            }
            // faq 탭일 경우
            if (modalType === 'faq') {
                const modal = document.querySelector(".faqDtlModal");
                const titleInput = modal.querySelector("input[name='question']");
                const contentTextarea = modal.querySelector("textarea[name='answer']");
                if (updateBtn.textContent === '수정') {
                    // 입력 필드를 수정 가능하도록 활성화
                    titleInput.disabled = false;
                    contentTextarea.disabled = false;
                    updateBtn.textContent = '저장';  // 버튼 텍스트를 '저장'으로 변경
                } else {
                    // 입력값 검증
                    const question = titleInput.value.trim();
                    const answer = contentTextarea.value.trim();

                    if (!question || !answer) {
                        alert('질문과 답변을 모두 입력해주세요.');
                        return;
                    }
                    // 제목과 내용 값을 가져와 수정된 데이터로 서버에 전송
                    const updatedFAQData = {
                        faqId: modal.querySelector("input[name='faqId']").value,
                        question: titleInput.value,
                        answer: contentTextarea.value
                    };
                    await axios({
                        method: 'put',
                        url: '/board/faqUpdate',
                        data: updatedFAQData,
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    }).then(res => {
                        alert("FAQ 수정에 성공했습니다.");
                        loadFAQs();
                        titleInput.disabled = true;
                        contentTextarea.disabled = true;
                        updateBtn.textContent = '수정';
                        faqDtlModal.hide();
                    }).catch(e => {
                        alert("FAQ 수정에 실패했습니다.");
                    });
                }
            }
        })
    })

    document.querySelector("button[name='regResponse']").addEventListener('click', async function () {
        const modal = document.querySelector(".qnaDtlModal");
        const responseUpdateBtn = modal.querySelector("button[name='regResponse']");
        const responseInput = modal.querySelector("textarea[name='response']");
        if (responseUpdateBtn.textContent === '답변 등록') {
            // 입력 필드를 수정 가능하도록 활성화
            responseInput.disabled = false;
            responseUpdateBtn.textContent = '답변 저장';  // 버튼 텍스트를 '저장'으로 변경
        } else {
            // 제목과 내용 값을 가져와 수정된 데이터로 서버에 전송
            const updatedQnAResponseData = {
                qnaId: modal.querySelector("input[name='qnaId']").value,
                userId: modal.querySelector("input[name='userId']").value,
                response: responseInput.value
            };
            await axios({
                method: 'put',
                url: '/board/qnaResponseUpdate',
                data: updatedQnAResponseData,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(res => {
                alert("Q&A 답변 등록에 성공했습니다.");
                responseInput.disabled = true;
                responseUpdateBtn.textContent = '답변 등록';
                qnaDtlModal.hide();
                loadQnas().then(res => {
                    if (res !== "") {
                        qnaGrid.resetData(res);  // grid에 세팅
                    }
                }).catch(e => {
                    alert("Q&A 답변 등록에 실패했습니다.");
                });

            }).catch(e => {
                alert("Q&A 답변 등록에 실패했습니다.");
            });
        }
    })

}
window.onload = () => {
    init();
}