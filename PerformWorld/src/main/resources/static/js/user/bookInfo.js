const init = () => {

    getBknInfo().then(res => {
        document.querySelector("input[name='eventInfo']").value = res.eventInfo;
        document.querySelector("input[name='eventLocation']").value = res.eventLocation;
        document.querySelector("input[name='eventDate']").value = res.eventDate;
        document.querySelector("input[name='eventCast']").value = res.eventCast;
        document.querySelector("input[name='seats']").value = res.seatIds.join(', ');
        document.querySelector("input[name='address']").value = res.address;
        document.querySelector("input[name='userName']").value = res.userName;
        document.querySelector("input[name='regDate']").value = res.regDate.split('T')[0];
        document.querySelector("input[name='status']").value = res.status;
        document.querySelector("input[name='paymentAmount']").value = res.payment.paymentAmount.toLocaleString() + "원";
        document.querySelector("input[name='paymentMethod']").value = res.payment.paymentMethod;
        document.querySelector("input[name='paymentDate']").value = res.payment.paymentDate.split('T')[0];

        // 최소 관람일 하루 전이면서 예매 상태가 취소가 아닐 때만 취소 버튼 활성화
        if (res.status !== "취소") {
            const eventDate = new Date(res.eventDate.split(" ")[0]);
            const tomorrow = new Date();
            tomorrow.setDate(tomorrow.getDate() + 1); // 내일 날짜
            tomorrow.setHours(9,0,0,0);

            if (eventDate >= tomorrow) {
                document.querySelector(".cancelDiv").style.display = "inline-block";
            }
        }

    }).catch(e => {
        alert("상세내역을 가져오는데 실패했습니다.");
    });

    // 예매 취소
    document.querySelector(".cancelBtn").addEventListener("click", function (e) {
        if(confirm("정말 예매를 취소하시겠습니까?")) {
            cancelBooking().then(res => {
                alert("예매 취소에 성공했습니다.");
                window.location.href = `/user/mypage`;
            }).catch(e => {
                alert("예매 취소에 실패했습니다.");
            });
        }
    });

    // 예매 상세정보 조회
    async function getBknInfo() {
        const res = await axios({
            method : 'post',
            url : '/pay/getBknInfo',
            data : { bookingId: bookingId },
            headers : {
                'Content-Type' : 'application/json'
            }
        });
        return res.data;
    }

    // 예매 취소
    async function cancelBooking() {
        const res = await axios({
            method : 'post',
            url : '/pay/cancel',
            data : { userId: user.uid, bookingId: bookingId },  // loginInfo
            headers : {
                'Content-Type' : 'application/json'
            }
        });
        return res.data;
    }
}

window.onload = () => {
    init();
}