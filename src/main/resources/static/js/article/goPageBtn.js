/* 마이페이지 이동 */
const gnbWrap = document.getElementById("gnbWrap");
const mProfileImgSideNav = gnbWrap.querySelector(".mProfileImg");
const mNameSideNav = gnbWrap.querySelector(".mName");
const mNickSideNav = gnbWrap.querySelector(".mNick");

const memberInfoSideNav = gnbWrap.querySelector(".memberInfo");

mProfileImgSideNav.addEventListener("click",function(){
  location.href = "/member/myPage/" + memberInfoSideNav.dataset.memberid;
});
mNameSideNav.addEventListener("click",function(){
  location.href = "/member/myPage/" + memberInfoSideNav.dataset.memberid;
});
mNickSideNav.addEventListener("click",function(){
  location.href = "/member/myPage/" + memberInfoSideNav.dataset.memberid;
});

// function goMyPage(){
//   myPageForm.method = "POST";
//   myPageForm.action = "/member/myPage";
//   myPageForm.submit();
// }