
let smsModalStatus = false;

const smsAuthForm = document.smsAuthForm; // 휴대폰 인증 form
const smsAuthFormWrap = smsAuthForm.querySelector(".sms-wrap");

// 모달 창
const smsModal = document.getElementById("detail-page-modal-2");
// 번호 인증하기 시작 버튼
const smsAuthBtn = detailTable.querySelector(".sms-auth-Btn");

smsAuthForm.tel.addEventListener("keydown", (e)=>{ // 엔터의 원래 이벤트 방지
  if( window.event.keyCode == 13 ){
    e.preventDefault();
  }
});

smsAuthBtn.addEventListener("click", ()=>{
  console.log("click..!!");
  let buttonTamp = `<button type="button" class="sms-check-btn">인증 문자 보내기</button>`;
  smsAuthFormWrap.insertAdjacentHTML("beforeend", buttonTamp);
  smsModalToggle(); // 모달 키기
  smsAuthSend(); // 버튼 클릭 이벤트 추가해주기
})

function smsAuthSend(){ // [인증 문자 보내기] 버튼 클릭 이벤트
  let smsCheckBtn = smsAuthForm.querySelector(".sms-check-btn");
  smsCheckBtn.addEventListener("click", ()=>{
    if(phoneNumValidate()) { // 휴대폰 번호 유효성 검사
      sendSMS("send"); // 비동기 통신 - 문자 보내기
    }
  });
}

function sendSMS(btnType){
  const url = "/api/members/sms/" + btnType;
  fetch(url, {
    method: "post", // PATCH 요청
    body: JSON.stringify({
      tel: smsAuthForm.tel.value,
      authCode: smsAuthForm.authCode.value
    }),
    headers: {
      "Content-type": "application/json"
    }
  }).then(response => {
    // http 응답 코드에 따른 메세지 출력
    if(response.status == 200){
      if(btnType == "send"){ // 문자 송신 이벤트일 때
        alert("인증 문자가 전송됐습니다.");
        authNumCheck(); // 폼 변경
      } else if(btnType == "confirm"){
        alert("문자 인증에 성공했습니다.");
        location.reload(); // 현재 페이지 새로고침
      }
    } else if(response.status == 226){
      alert("문자 인증이 불가능한 번호입니다.");
    } else if(response.status == 400){
      alert("인증 번호가 일치하지 않습니다.");
    }
  });
}

function authNumCheck(){
  // lable 정보 변겅
  smsAuthForm.querySelector("label").innerText = "인증 번호 입력";
  // input tag 정보 변경
  smsAuthForm.tel.classList.add("not-active");
  // let inputTamp = `<input type="text" name="authCode" class="numInput detail-modal-input" placeholder="인증 번호 입력" maxlength="6">`;
  // smsAuthFormWrap.insertAdjacentHTML("beforeend", inputTamp);
  smsAuthForm.authCode.classList.remove("not-active");
  // 버튼 정보 변경
  smsAuthFormWrap.querySelector("button").remove();
  let buttonTamp = `<button type="button" class="sms-confirm-btn">인증하기</button>`;
  smsAuthFormWrap.insertAdjacentHTML("beforeend", buttonTamp);

  smsModalStatus = true; // 새 폼으로 변화시

  let smsConfirmBtn = smsAuthFormWrap.querySelector(".sms-confirm-btn");
  smsConfirmBtn.addEventListener("click", ()=>{
    sendSMS("confirm");
  })
}

function phoneNumValidate(){
  let phoneValue = smsAuthForm.tel.value;

  let repExpPhone = /^[0-9]{11,12}/;
  if(!repExpPhone.test(phoneValue)){ // 유효성검사
    alert("휴대폰 번호는 11~12자 사이의 숫자로 입력 가능합니다.");
    smsAuthForm.tel.focus();
    return false;
  } else { // 검사 통과
    return true;
  }
}

/* 모달창 끄기 */
const smsModalCloseBtn = smsModal.querySelector(".smsModal-close-btn");
smsModalCloseBtn.addEventListener("click", ()=>{
  smsAuthForm.tel.value = ""; // 값 비워주기
  smsAuthFormWrap.querySelector("button").remove();
  if(smsModalStatus){ // 새 비번 입력폼이면 ...
    // 폼 정보 변경
    smsAuthForm.querySelector("label").innerText = "문자 인증";
    smsAuthForm.tel.value = "";
    smsAuthForm.tel.classList.remove("not-active");
    smsAuthForm.authCode.remove();
    smsAuthFormWrap.querySelector("button").remove();

    smsModalStatus = false; // 원래 폼으로 변경
  }
  smsModalToggle(); // 모달 끄기
});

function smsModalToggle(){ // 모달창 키고 끄는 토글
  // 모달 창 끄기 & 블러처리
  if(smsModal.getAttribute("class").includes("not-active")){
    smsModal.classList.remove("not-active");
    document.getElementById("wrap").style.filter = "blur(10px)";
    document.getElementById("wrap").style.filter = "blur(10px)";
  } else {
    smsModal.classList.add("not-active");
    document.getElementById("wrap").style.filter = "none";
    document.getElementById("wrap").style.filter = "none";
  }
}

/* 휴대폰 번호 숫자만 입력되도록 */
smsAuthForm.tel.addEventListener("input", (e)=>{
  e.currentTarget.value =
    e.currentTarget.value.replace(/[^0-9]/g, '');
});
