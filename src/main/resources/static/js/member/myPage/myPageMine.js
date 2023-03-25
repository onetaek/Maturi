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