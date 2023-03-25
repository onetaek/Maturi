
const detailTable = document.getElementById("detail-info");
const withdrawalModal = document.getElementById("withdrawal-modal");

const emailValue = detailTable.querySelector(".email-value").innerText;

/* 회원탈퇴 클릭 */
const withdrawalBtn = detailTable.querySelector(".withdrawal-btn");
withdrawalBtn.addEventListener("click", ()=>{
  console.log(emailValue);
  if(emailValue.includes("@k.com") || emailValue.includes("@n.com")){
    // withdrawalAction();
  } else {
    // 모달창 키기
    withdrawalModal.classList.remove("not-active");
    // 블러처리
    document.getElementById("wrap").style.filter = "blur(10px)";
    document.getElementById("wrap").style.filter = "blur(10px)";
  }
});

/* 회원탈퇴하기 */
const withdrawalForm = document.withdrawalForm;
const withdrawalSubmit = withdrawalForm.querySelector(".withdrawal-submit-btn");
withdrawalSubmit.addEventListener("click", ()=>{
  if(withdrawalForm.passwd.value != null && withdrawalForm.passwd.value != "") {
    const url = `/api/members/passwdCheck`;
    fetch(url, {
      method: "post", // PATCH 요청
      body: JSON.stringify({
        passwd: withdrawalForm.passwd.value
      }),
      headers: {
        "Content-type": "application/json"
      }
    }).then(response => {
      // http 응답 코드에 따른 메세지 출력
      if (!response.ok) { // 비밀번호 불일치
        alert("비밀번호가 일치하지 않습니다.");
      } else { // 비밀번호 일치
        withdrawalAction();
      }
    });
  }
});

function modalToggle(){

  // 모달창 끄기
  withdrawalModal.classList.add("not-active");
  // 블러처리
  document.getElementById("wrap").style.filter = "none";
  document.getElementById("wrap").style.filter = "none";

}

function withdrawalAction(){
  confirm("정말 회원탈퇴를 진행하시겠습니까?");
  // withdrawalForm.action = "/member/withdrawal";
  // withdrawalForm.method = "post";
  // withdrawalForm.submit();
}

