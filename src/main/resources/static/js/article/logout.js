const logoutBtn = document.querySelector(".logout-btn");
const logoutForm = document.logoutForm;

logoutBtn.addEventListener("click",function(){
    if(confirm("진짜 로그아웃 할꺼?")){
        logoutForm.method = "POST";
        logoutForm.action = "/member/logout";
        logoutForm.submit();
    }
});