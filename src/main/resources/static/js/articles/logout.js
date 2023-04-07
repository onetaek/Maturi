const logoutBtn = document.querySelector(".logout-btn");
const logoutForm = document.logoutForm;

logoutBtn.addEventListener("click",function(){
    Swal.fire({
        title: "로그아웃 하시겠습니까?",
        icon: "question",
        showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
        confirmButtonColor: '#d33', // confrim 버튼 색깔 지정
        cancelButtonColor: '#6e7881', // cancel 버튼 색깔 지정
        confirmButtonText: '로그아웃', // confirm 버튼 텍스트 지정
        cancelButtonText: '취소', // cancel 버튼 텍스트 지정
        reverseButtons: false, // 버튼 순서 거꾸로
        // background-color: #6e7881
    }).then((result) => {
        if (result.isConfirmed) {
            logoutForm.method = "POST";
            logoutForm.action = "/members/logout";
            logoutForm.submit();
        }
    });

});