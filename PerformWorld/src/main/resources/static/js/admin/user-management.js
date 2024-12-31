function deleteUser(button) {
    var userId = button.getAttribute('data-user-id');
    if (confirm('정말 이 사용자를 삭제하시겠습니까?')) {
        // Ajax를 통해 서버로 삭제 요청을 보냄
        fetch('/admin/user/' + userId, {  // URL 경로 수정
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    alert('사용자가 삭제되었습니다.');
                    location.reload(); // 페이지 새로고침하여 테이블 갱신
                } else {
                    alert('사용자 삭제에 실패했습니다.');
                }
            })
            .catch(error => {
                alert('오류가 발생했습니다.');
            });
    }
}