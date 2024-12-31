// 사용자 정보로 모달 열기 (행 클릭)
function openEditUserModal(userId) {
    // AJAX 요청으로 사용자 정보를 불러옵니다
    fetch(`/admin/getUser/${userId}`)
        .then(response => response.json())
        .then(user => {
            // 사용자 정보 모달에 채우기
            document.getElementById('editUserId').value = user.userId;
            document.getElementById('editName').value = user.name;
            document.getElementById('editEmail').value = user.email;
            document.getElementById('editPhoneNumber').value = user.phoneNumber;
            document.getElementById('editAddress1').value = user.address1;
            document.getElementById('editAddress2').value = user.address2;
            document.getElementById('editPostcode').value = user.postcode;
            document.getElementById('editTierName').value = user.tierName;

            // 모달 열기
            document.getElementById('editUserModal').style.display = 'block';
        })
        .catch(error => console.error('Error fetching user data:', error));
}

// 수정 모달 닫기
function closeEditUserModal() {
    document.getElementById('editUserModal').style.display = 'none';
}

// 사용자 수정 폼 제출
document.getElementById('editUserForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const userData = {
        userId: document.getElementById('editUserId').value,
        name: document.getElementById('editName').value,
        email: document.getElementById('editEmail').value,
        phoneNumber: document.getElementById('editPhoneNumber').value,
        address1: document.getElementById('editAddress1').value,
        address2: document.getElementById('editAddress2').value,
        postcode: document.getElementById('editPostcode').value,
        tierName: document.getElementById('editTierName').value
    };

    fetch('/admin/updateUser', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    })
        .then(response => response.json())
        .then(data => {
            alert('사용자 정보가 수정되었습니다.');
            closeEditUserModal();
            window.location.reload(); // 페이지 리로드하여 변경사항 반영
        })
        .catch(error => console.error('Error:', error));
});