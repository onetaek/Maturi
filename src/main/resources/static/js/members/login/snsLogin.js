
const snsLoginForm = document.snsLoginForm;
const kakaoLoginBtn = document.querySelector(".kakao-login");
const naverLoginBtn = document.querySelector(".naver-login");

kakaoLoginBtn.addEventListener("click",()=>{
    snsLoginForm.method="POST";
    snsLoginForm.action="/oauth/kakao/login";
    snsLoginForm.submit();
})

naverLoginBtn.addEventListener("click",()=>{
    snsLoginForm.method="POST";
    snsLoginForm.action="/oauth/naver/login";
    snsLoginForm.submit();
});

