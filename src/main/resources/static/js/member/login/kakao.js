const kakaoLogin = document.querySelector(".kakao-login");
kakaoLogin.addEventListener("click",()=>{
    location.href="https://kauth.kakao.com/oauth/authorize?client_id=4f96f770ffd075880f40824acb785f43&redirect_uri=http://localhost:8080/kakao/callback&response_type=code"
})