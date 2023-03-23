/* 마이페이지 이동 */
const gnbWrap = document.getElementById("gnbWrap");
const mProfileImgSideNav = gnbWrap.querySelector(".mProfileImg");
const mNameSideNav = gnbWrap.querySelector(".mName");
const mNickSideNav = gnbWrap.querySelector(".mNick");
const myPageForm = document.goMyPageForm;

mProfileImgSideNav.addEventListener("click",function(){
  location.href = "/member/myPage";
});
mNameSideNav.addEventListener("click",function(){
  location.href = "/member/myPage";
});
mNickSideNav.addEventListener("click",function(){
  location.href = "/member/myPage";
});

// function goMyPage(){
//   myPageForm.method = "POST";
//   myPageForm.action = "/member/myPage";
//   myPageForm.submit();
// }