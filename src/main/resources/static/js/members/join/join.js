const joinForm = document.joinForm;
const submitBtn = joinForm.submitBtn;

let emailConfirmCheck = false;
// submit 하기
submitBtn.addEventListener("click", ()=>{
  if(emailConfirmCheck){
    // post 타입으로 submit
    joinForm.method = "post";
    joinForm.action = "/members/join";
    joinForm.submit();
  }else{
    Swal.fire({
      title: "이메일을 미인증",
      text:"회원가입에 사용할 이메일 주소 입력 후, 인증번호 받기를 클릭해 주세요.",
      icon: 'warning',
      confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
    })
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
    Swal.fire({
      title: "이메일을 양식에 맞게 입력해주세요.",
      icon: 'warning',
      confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
    })
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
      if(response.ok){
        Swal.fire({
          title: "이메일 인증 메일이 전송되었습니다. 이메일을 확인하여 주세요.",
          icon: 'info',
          confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
        }).then(function(){
          // 인증 버튼 이벤트 추가
          confirmCheck();
        })
      }else{
        Swal.fire({
          title: "해당 이메일은 이미 가입되어있습니다.",
          icon: 'info',
          confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
        }).then(function(){
          window.location.href='/members/login';
        });
      }
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
        joinForm.email.setAttribute("disabled", "disabled");
        joinForm.emailConfirm.setAttribute("disabled", "disabled");

        document.querySelector(".email-wrap").style.height = "44px";
        document.querySelector(".email-confirm-wrap").style.height = "44px";

        emailConfirmCheck = true;
        Swal.fire({
          title: msg,
          icon: 'success',
          confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
        })
      }
      else {
        msg = "인증번호가 일치하지 않습니다!";
        Swal.fire({
          title: msg,
          icon: 'warning',
          confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
        })
      }

    })
  })
}
// 정규식
// let reqExpPasswd = /^(?=.*[a-z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}/;
