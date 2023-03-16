console.log("snsLogin.js");

const snsLoginForm = document.snsLoginForm;
const kakaoLoginBtn = document.querySelector(".kakao-login");
const naverLoginBtn = document.querySelector(".naver-login");

console.log("snsLoginForm",snsLoginForm);

kakaoLoginBtn.addEventListener("click",()=>{
    location.href="https://kauth.kakao.com/oauth/authorize?client_id=4f96f770ffd075880f40824acb785f43&redirect_uri=http://localhost:8080/kakao/callback&response_type=code"
})

naverLoginBtn.addEventListener("click",()=>{
    snsLoginForm.method="POST";
    snsLoginForm.action="/oauth/naver/login";
    snsLoginForm.submit();
});