const logoutBtn = document.querySelector(".logout-btn");
const logoutForm = document.logoutForm;

logoutBtn.addEventListener("click",function(){
    if(confirm("로그아웃 하시겠습니까?")){
        logoutForm.method = "POST";
        logoutForm.action = "/member/logout";
        logoutForm.submit();
    }
});