document.addEventListener("DOMContentLoaded", () => {
    const faqForm = document.getElementById("faqForm");
    const registerBtn = document.querySelector(".registerBtn");
    const faqTableBody = document.querySelector("table tbody");

    // Load FAQs when the page loads
    loadFAQs();

    // Register button event listener
    registerBtn.addEventListener("click", (e) => {
        e.preventDefault();
        registerFAQ();
    });

    // Function to load FAQs dynamically
    async function loadFAQs() {
        try {
            const response = await axios.get("/faq/list"); // Replace with your actual endpoint
            const faqs = response.data;

            faqTableBody.innerHTML = ""; // Clear existing rows
            faqs.forEach((faq) => {
                const row = `
                    <tr>
                        <td>${faq.faqId}</td>
                        <td>${faq.question}</td>
                        <td>${faq.answer}</td>
                        <td>${faq.createdAt}</td>
                        <td>${faq.updatedAt}</td>
                        <td><button class="updateBtn" data-id="${faq.faqId}">수정</button></td>
                        <td><button class="deleteBtn" data-id="${faq.faqId}">삭제</button></td>
                    </tr>
                `;
                faqTableBody.insertAdjacentHTML("beforeend", row);
            });

            // Add event listeners for update and delete buttons
            document.querySelectorAll(".updateBtn").forEach((btn) =>
                btn.addEventListener("click", handleUpdate)
            );
            document.querySelectorAll(".deleteBtn").forEach((btn) =>
                btn.addEventListener("click", handleDelete)
            );
        } catch (error) {
            console.error("Error loading FAQs:", error);
            alert("FAQ 목록을 불러오는 중 오류가 발생했습니다.");
        }
    }

    // Function to register a new FAQ
    async function registerFAQ() {
        const question = faqForm.question.value.trim();
        const answer = faqForm.answer.value.trim();

        if (!question || !answer) {
            alert("질문과 답변을 입력해주세요.");
            return;
        }

        try {
            await axios.post("/faq/register", { question, answer });
            alert("FAQ가 성공적으로 등록되었습니다.");
            faqForm.reset(); // Clear the form
            loadFAQs(); // Reload FAQ list
        } catch (error) {
            console.error("Error registering FAQ:", error);
            alert("FAQ 등록 중 오류가 발생했습니다.");
        }
    }

    // Function to handle update
    async function handleUpdate(event) {
        const faqId = event.target.getAttribute("data-id");
        const newQuestion = prompt("새 질문을 입력하세요:");
        const newAnswer = prompt("새 답변을 입력하세요:");

        if (!newQuestion || !newAnswer) {
            alert("질문과 답변을 모두 입력해야 합니다.");
            return;
        }

        try {
            await axios.put(`/faq/update/${faqId}`, {
                question: newQuestion,
                answer: newAnswer,
            });
            alert("FAQ가 성공적으로 수정되었습니다.");
            loadFAQs(); // Reload FAQ list
        } catch (error) {
            console.error("Error updating FAQ:", error);
            alert("FAQ 수정 중 오류가 발생했습니다.");
        }
    }

    // Function to handle delete
    async function handleDelete(event) {
        const faqId = event.target.getAttribute("data-id");
        const confirmDelete = confirm("이 FAQ를 삭제하시겠습니까?");

        if (!confirmDelete) return;

        try {
            await axios.delete(`/faq/delete/${faqId}`);
            alert("FAQ가 성공적으로 삭제되었습니다.");
            loadFAQs(); // Reload FAQ list
        } catch (error) {
            console.error("Error deleting FAQ:", error);
            alert("FAQ 삭제 중 오류가 발생했습니다.");
        }
    }
});
