// 사용자 삭제 함수
function deleteUser(button) {
    var userId = button.getAttribute('data-user-id');
    if (confirm('정말 이 사용자를 삭제하시겠습니까?')) {
        // Axios를 통해 서버로 삭제 요청을 보냄
        axios.delete('/admin/user/' + userId)
            .then(response => {
                if (response.status === 200) {
                    alert('사용자가 삭제되었습니다.');
                    window.location.reload();  // 페이지 전체 새로 고침
                } else {
                    alert('사용자 삭제에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error("사용자 삭제 오류:", error);
                alert('오류가 발생했습니다.');
            });
    }
}