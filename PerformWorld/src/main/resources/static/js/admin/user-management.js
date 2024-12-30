// 사용자 추가 모달 열기
document.querySelector('.add-user-btn').addEventListener('click', function() {
    document.getElementById('addUserModal').style.display = 'block';

    // Tier ID 기본값을 1로 설정
    document.getElementById('tierId').value = 1;
});

// 사용자 수정 모달 열기
document.querySelectorAll('.edit-user-btn').forEach(function(button) {
    button.addEventListener('click', function() {
        const userId = button.getAttribute('data-id');

        // 사용자 정보를 로드하여 수정 모달에 표시
        fetch(`/admin/getUser/${userId}`)
            .then(response => response.json())
            .then(user => {
                document.getElementById('editUserId').value = user.userId;
                document.getElementById('editName').value = user.name;
                document.getElementById('editEmail').value = user.email;
                document.getElementById('editPhoneNumber').value = user.phoneNumber;
                document.getElementById('editAddress1').value = user.address1;
                document.getElementById('editAddress2').value = user.address2;
                document.getElementById('editPostcode').value = user.postcode;
                document.getElementById('editTierName').value = user.tierName;
                document.getElementById('editUserModal').style.display = 'block';
            });
    });
});

// 사용자 추가 폼 제출
document.getElementById('addUserForm').addEventListener('submit', function(event) {
    event.preventDefault();  // 폼이 기본적으로 제출되지 않도록 합니다.

    const userId = document.getElementById('userId').value;
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const phoneNumber = document.getElementById('phoneNumber').value;
    const address1 = document.getElementById('address1').value;
    const address2 = document.getElementById('address2').value;
    const postcode = document.getElementById('postcode').value;
    const tierId = document.getElementById('tierId').value;  // tierId 추가

    fetch('/admin/addUser', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            userId: userId,
            name: name,
            email: email,
            password: password,
            phoneNumber: phoneNumber,
            address1: address1,
            address2: address2,
            postcode: postcode,
            tierId: tierId  // tierId 전송
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data) {
                alert('사용자가 추가되었습니다.');
                location.reload();  // 페이지를 새로 고쳐서 추가된 사용자 목록을 표시
            }
        })
        .catch(error => console.error('Error:', error));
});

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
            window.location.reload(); // 페이지 리로드하여 변경사항 반영
        })
        .catch(error => console.error('Error:', error));
});

// 모달 닫기
function closeAddUserModal() {
    document.getElementById('addUserModal').style.display = 'none';
}

function closeEditUserModal() {
    document.getElementById('editUserModal').style.display = 'none';
}