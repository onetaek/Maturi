const pwInquiryForm = document.pwInquiryForm;

let emailConfirmCheck = false;

/* 이메일 인증 */
// 인증 번호 전송
const emailAuthBtn = document.querySelector(".email-send-btn");
const confirmCheckBtn = document.querySelector(".confirm-check-btn");
emailAuthBtn.addEventListener("click", ()=>{
  let regExpEmail = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;
  // 유효성 검사
  if(!regExpEmail.test(pwInquiryForm.email.value)){
    alert("이메일을 양식에 맞게 입력해주세요.");
    pwInquiryForm.email.focus();
    return;
  }
  else {
    const url = "/api/members/help/pwInquiry/emailAuth";
    fetch(url, {
      method: "post",
      body: JSON.stringify({
        email: pwInquiryForm.email.value
      }),
      headers: {
        "Content-type": "application/json"
      }
    }).then(response => {
      // http 응답 코드에 따른 메세지
      console.log(response);
      const msg = (response.ok) ?
        "이메일 인증 메일이 전송되었습니다. 이메일을 확인하여 주세요." :
        "가입된 정보가 존재하지 않습니다.";
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
        emailConfirm: pwInquiryForm.emailConfirm.value
      }),
      headers: {
        "Content-type": "application/json"
      }
    }).then(response => {
      // http 응답 코드에 따른 메세지
      if(response.ok){
        alert("이메일 인증에 성공하셨습니다! 변경할 비밀번호를 입력해주세요.");
        pwInquiryForm.email.setAttribute("readonly", "readonly");
        pwInquiryForm.emailConfirm.setAttribute("disabled", "disabled");
        document.querySelector(".confirmWrap").style.height = "44px";
        emailConfirmCheck = true;

        // 비번/비번확인 태그 활성화
        const notActiveTag = document.querySelectorAll(".not-active");
        notActiveTag.forEach(tag => {
          tag.classList.remove("not-active");
        })

        /* 추가된 버튼에 이벤트 추가 */
        pwInquiryFormSubmit();
      }
      else {
        alert("인증번호가 일치하지 않습니다!");
      }
    })
  })
}

/* submit 이벤트 */
function pwInquiryFormSubmit(){
  pwInquiryForm.submitBtn.addEventListener("click", ()=>{
    if(passwdCheckValidate()){ // 비밀번호 유효성 검사
      if(emailConfirmCheck){ // 이메일 인증 검사
        // post 타입으로 submit
        pwInquiryForm.method = "post";
        pwInquiryForm.action = "/members/help/passwd";
        pwInquiryForm.submit();
      }
    } else {
    }

  });
}

/* 비밀번호 유효성 검사 */
function passwdCheckValidate(){
  let passwdValue = pwInquiryForm.passwd.value;
  let passwdCheckValue = pwInquiryForm.passwdCheck.value;

  let repExpPasswd = /(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{8,20}/;
  if(!repExpPasswd.test(passwdValue)){ // 유효성검사
    alert("비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.");
    pwInquiryForm.passwd.focus();
    return false;
  } else if(passwdValue != passwdCheckValue){ // 일치 여부
    alert("비밀번호가 일치하지 않습니다. 다시 입력하세요.");
    pwInquiryForm.passwdCheck.focus();
    return false;
  } else { // 검사 통과
    return true;
  }
}