const emailInquiryForm = document.emailInquiryForm;

let smsConfirmCheck = false;

/* 문자 인증 */
// 인증 번호 전송
const emailAuthBtn = document.querySelector(".email-send-btn");
emailAuthBtn.addEventListener("click", ()=>{
  const url = "/api/members/help/emailInquiry/sms/send";
  fetch(url, {
    method: "post",
    body: JSON.stringify({
      tel: emailInquiryForm.contact.value
    }),
    headers: {
      "Content-type": "application/json"
    }
  }).then(response => {
    // http 응답 코드에 따른 메세지
    if(response.status == 200){ // 문자 전송 성공
      alert("인증 문자가 전송됐습니다.");
    } else if(response.status == 404){ // 회원 정보 못찾음
      alert("해당 휴대폰 번호로 가입된 아이디(이메일)이 없습니다.");
    } else if(response.status == 401){ // 소셜 로그인일 경우 .. (문자 전송 x)
      if(!alert("해당 휴대폰 번호는 소셜 로그인(카카오/네이버)으로 가입되어 있습니다.")){
        location.href = "/members/login";
      }
    }

    // [이메일 찾기]버튼에 이벤트 추가
    confirmCheck();
  })
});

function confirmCheck(){
  emailInquiryForm.submitBtn.addEventListener("click", ()=>{
    const checkUrl = "/api/members/help/emailInquiry/sms/confirm";
    fetch(checkUrl, {
      method: "post",
      body: JSON.stringify({
        tel: emailInquiryForm.contact.value,
        authCode: emailInquiryForm.authCode.value
      }),
      headers: {
        "Content-type": "application/json"
      }
    }).then((response) => response.json())
      .then((data) => {
        if(data.email == null){
          alert("인증 코드가 일치하지 않습니다.");
        } else {
          if(!alert("인증 완료!!")){
            smsConfirmCheck = true;
            emailInquiryFormSubmit(data.email);
          }
        }
      })
  })
}

/* submit 이벤트 */
function emailInquiryFormSubmit(email){
  if(smsConfirmCheck){ // 이메일 인증 검사
    emailInquiryForm.email.value = email;
    // post 타입으로 submit
    emailInquiryForm.method = "post";
    emailInquiryForm.action = "/members/help/email";
    emailInquiryForm.submit();
  }

}

/* 휴대폰 번호 숫자만 입력되도록 */
emailInquiryForm.contact.addEventListener("input", (e)=>{
  e.currentTarget.value =
    e.currentTarget.value.replace(/[^0-9]/g, '');
});