/* 더보기 버튼 */
const ellipsisBtn = document.querySelectorAll(".ellipsis-btn");
const ellipsisContent = document.querySelectorAll(".ellipsis-content");

for(let i = 0 ; i < ellipsisBtn.length; i++){
  ellipsisBtn[i].addEventListener("click",function(){
    ellipsisContent[i].classList.toggle("active");
  });
}

// const moreBtn = document.querySelector(".moreBtn");
// const memberBtnUl = document.querySelector(".memberBtn ul");
//
// let ulDisplay = "none"; // 초기값 설정
// moreBtn.addEventListener("click", ()=>{
//
//   if(ulDisplay == 'none'){
//     ulDisplay = "block";
//   }
//   else {
//     ulDisplay = "none";
//   }
//   memberBtnUl.style.display = ulDisplay;
// });


//////////////// 로그인 멤버의 마이페이지 //////////////////
/* 세부 정보 버튼 클릭 */
const detailInfo = document.querySelector(".memberInfo .detailInfo");
detailInfo.addEventListener("click", function (){
  location.href = "/member/myPage/detail";
})

/* 프로필 편집 버튼 클릭 */
const editMyPage = document.querySelector(".editMyPage");
editMyPage.addEventListener("click", (e)=>{
  e.preventDefault();
  location.href = "/member/myPage/edit";
})