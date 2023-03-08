const moreBtn = document.querySelector(".moreBtn");
const memberBtnUl = document.querySelector(".memberBtn ul");

let ulDisplay = "none"; // 초기값 설정
moreBtn.addEventListener("click", ()=>{

  if(ulDisplay == 'none'){
    ulDisplay = "block";
  }
  else {
    ulDisplay = "none";
  }
  memberBtnUl.style.display = ulDisplay;
});