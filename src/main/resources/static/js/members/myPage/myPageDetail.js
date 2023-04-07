
let modalStatus = false;

const detailTable = document.getElementById("detail-info");
const detailModalForm = document.modalForm;
const detailModalFormWrap = detailModalForm.querySelector(".password-wrap");

// 소셜 로그인 확인을 위한 이메일 value
const emailValue = detailTable.querySelector(".email-value").innerText;
detailModalForm.passwd.addEventListener("keydown", (e)=>{ // 엔터의 원래 이벤트 방지
  if( window.event.keyCode == 13 ){
    e.preventDefault();
  }
});

function newPasswd(){
  let passwdCheckBtn = detailModalForm.querySelector(".passwd-check-btn");
  passwdCheckBtn.addEventListener("click", ()=>{
    if(detailModalForm.passwd.value != null && detailModalForm.passwd.value != "") {
      checkPasswd("newPasswd");
    }
  });
}

/* 회원탈퇴 클릭 */
const detailModal = document.getElementById("detail-page-modal");
const withdrawalBtn = detailTable.querySelector(".withdrawal-btn");

withdrawalBtn.addEventListener("click", ()=>{
  // 소셜 로그인 -> 바로 회원 탈퇴
  if(emailValue =="KAKAO Login" || emailValue =="NAVER Login"){
    withdrawalAction(); // 회원탈퇴
  } else { // 사용자 비번 확인하기
    let buttonTamp = `<button type="button" class="withdrawal-submit-btn">탈퇴하기</button>`;
    detailModalFormWrap.insertAdjacentHTML("beforeend", buttonTamp);
    unregister(); // 회원 탈퇴 이벤트 추가
    modalToggle(detailModalForm);
  }
});

/* 모달창 끄기 */
const withdrawalCloseBtn = detailModal.querySelector(".withdrawal-close-btn");
withdrawalCloseBtn.addEventListener("click", ()=>{
  detailModalForm.passwd.value = ""; // 값 비워주기
  detailModalFormWrap.querySelector("button").remove();
  if(modalStatus){ // 새 비번 입력폼이면 ...
    detailModalFormWrap.querySelector(".passwdCheck").remove();
    detailModalForm.querySelector("label").innerText = "비밀번호 확인";
    modalStatus = false;
  }
  modalToggle(detailModal);
});


/* 회원탈퇴하기 */
function unregister(){
  let withdrawalSubmit = detailModalForm.querySelector(".withdrawal-submit-btn");
  withdrawalSubmit.addEventListener("click", ()=>{
    if(detailModalForm.passwd.value != null && detailModalForm.passwd.value != "") {
      checkPasswd("unregister"); // 비번 확인 체크
    }
  });
}

function modalToggle(modalWrap){ // 모달창 키고 끄는 토글
  // 모달 창 끄기 & 블러처리
  if(detailModal.getAttribute("class").includes("not-active")){
    detailModal.classList.remove("not-active");
    document.getElementById("wrap").style.filter = "blur(10px)";
    document.getElementById("wrap").style.filter = "blur(10px)";
  } else {
    detailModal.classList.add("not-active");
    document.getElementById("wrap").style.filter = "none";
    document.getElementById("wrap").style.filter = "none";
  }
}

function checkPasswd(btnType){
  let status = false;
  const url = "/api/members/passwdCheck";
  fetch(url, {
    method: "post", // PATCH 요청
    body: JSON.stringify({
      passwd: detailModalForm.passwd.value
    }),
    headers: {
      "Content-type": "application/json"
    }
  }).then(response => {
    // http 응답 코드에 따른 메세지 출력
    if (!response.ok) { // 비밀번호 불일치
      Swal.fire({
        title: "비밀번호가 일치하지 않습니다.",
        icon: 'warning',
        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
      })
    } else { // 비밀번호 일치
      if(btnType == "unregister"){
      withdrawalAction();
      } else if(btnType == "newPasswd"){
        newPasswdForm();
      }
    }
  });
}
function newPasswdForm(){
  detailModalForm.querySelector("label").innerText = "새 비밀번호 입력";
  detailModalForm.passwd.value = "";
  detailModalFormWrap.querySelector("button").remove();

  let inputTamp = `<input type="password" name="passwdCheck" class="passwd passwdCheck detail-modal-input" placeholder="비밀번호 확인">`;
  detailModalFormWrap.insertAdjacentHTML("beforeend", inputTamp);
  let buttonTamp = `<button type="button" class="newPasswd-submit-btn">비밀번호 변경</button>`;
  detailModalFormWrap.insertAdjacentHTML("beforeend", buttonTamp);

  modalStatus = true; // 새 비밀번호 폼으로 변화시

  let newPasswdBtn = detailModalFormWrap.querySelector(".newPasswd-submit-btn");
  newPasswdBtn.addEventListener("click", ()=>{
    if(passwdCheckValidate()){ // 비번 유효성 검사하기
      console.log("유효성 검사 통과!");
      newPasswdAction();
    }

  })
}

function passwdCheckValidate(){
  let passwdValue = detailModalForm.passwd.value;
  let passwdCheckValue = detailModalForm.passwdCheck.value;

  let repExpPasswd = /(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{8,20}/;
  if(!repExpPasswd.test(passwdValue)){ // 유효성검사
    Swal.fire({
      title: "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.",
      icon: 'warning',
      confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
    })
    detailModalForm.passwd.focus();
    return false;
  } else if(passwdValue != passwdCheckValue){ // 일치 여부
    Swal.fire({
      title: "비밀번호가 일치하지 않습니다. 다시 입력하세요.",
      icon: 'warning',
      confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
    })
    detailModalForm.passwdCheck.focus();
    return false;
  } else { // 검사 통과
    return true;
  }
}
function newPasswdAction(){ // 비번 변경 액션
  Swal.fire({
    title: "비밀번호를 변경하시겠습니까??",
    icon: "question",
    showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
    confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
    cancelButtonColor: '#6e7881', // cancel 버튼 색깔 지정
    confirmButtonText: '변경하기', // confirm 버튼 텍스트 지정
    cancelButtonText: '취소', // cancel 버튼 텍스트 지정
    reverseButtons: false, // 버튼 순서 거꾸로
    // background-color: #6e7881
  }).then((result) => {
    if (result.isConfirmed) {
      detailModalForm.action = "/members/newPasswd";
      detailModalForm.method = "post";
      detailModalForm.submit();

    }
  });

}
function withdrawalAction(){ // 회원 탈퇴 액션
  Swal.fire({
    title: "정말 회원탈퇴를 진행하시겠습니까?",
    text: "회원탈퇴한 계정은 복구할 수 없습니다",
    icon: "warning",
    showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
    confirmButtonColor: '#d33', // confrim 버튼 색깔 지정
    cancelButtonColor: '#6e7881', // cancel 버튼 색깔 지정
    confirmButtonText: '회원탈퇴', // confirm 버튼 텍스트 지정
    cancelButtonText: '취소', // cancel 버튼 텍스트 지정
    reverseButtons: false, // 버튼 순서 거꾸로
    // background-color: #6e7881
  }).then((result) => {
    if (result.isConfirmed) {
      detailModalForm.action = "/members/unregister";
      detailModalForm.method = "post";
      detailModalForm.submit();
    }
  });

}

