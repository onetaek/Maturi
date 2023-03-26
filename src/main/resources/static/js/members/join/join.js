const joinForm = document.joinForm;
const submitBtn = joinForm.submitBtn;

let emailConfirmCheck = false;
// submit 하기
submitBtn.addEventListener("click", ()=>{
  if(emailConfirmCheck){
    // post 타입으로 submit
    joinForm.method = "post";
    joinForm.action = "/member/join";
    joinForm.submit();
  }
});

/* 이메일 인증 */
// 인증 번호 전송
const emailAuthBtn = document.querySelector(".email-send-btn");
const confirmCheckBtn = document.querySelector(".confirm-check-btn");
emailAuthBtn.addEventListener("click", ()=>{
  let regExpEmail = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;
  // 유효성 검사
  if(!regExpEmail.test(joinForm.email.value)){
    alert("이메일을 양식에 맞게 입력해주세요.");
    return;
  }
  else {
    const url = "/api/members/emailAuth";
    fetch(url, {
      method: "post",
      body: JSON.stringify({
        email: joinForm.email.value
      }),
      headers: {
        "Content-type": "application/json"
      }
    }).then(response => {
      // http 응답 코드에 따른 메세지
      console.log(response);
      const msg = (response.ok) ?
        "이메일 인증 메일이 전송되었습니다. 이메일을 확인하여 주세요." :
        "해당 이메일은 이미 가입되어있습니다.";
      alert(msg);

      // 인증 버튼 이벤트 추가
      confirmCheck();
    })
  }
});

function confirmCheck(){
  confirmCheckBtn.addEventListener("click", ()=>{
    const checkUrl = "/api/members/emailConfirm";
    fetch(checkUrl, {
      method: "post",
      body: JSON.stringify({
        emailConfirm: joinForm.emailConfirm.value
      }),
      headers: {
        "Content-type": "application/json"
      }
    }).then(response => {
      // http 응답 코드에 따른 메세지
      let msg = null;
      if(response.ok){
        msg = "이메일 인증에 성공하셨습니다!";
        joinForm.email.setAttribute("readonly", "readonly");
        joinForm.emailConfirm.setAttribute("disabled", "disabled");
        document.querySelector(".confirmWrap").style.height = "44px";
        emailConfirmCheck = true;
      }
      else {
        msg = "인증번호가 일치하지 않습니다!";
      }
      alert(msg);
    })
  })
}
// 정규식
// let reqExpPasswd = /^(?=.*[a-z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}/;
