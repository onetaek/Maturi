/* 비밀번호 변경 */
const changePwBtn = detailTable.querySelector(".change-passwd");
changePwBtn.addEventListener("click", ()=>{
  let buttonTamp = `<button type="button" class="passwd-check-btn">비밀번호 확인</button>`;
  detailModalFormWrap.insertAdjacentHTML("beforeend", buttonTamp);
  modalToggle(detailModalForm);
  newPasswd();
});