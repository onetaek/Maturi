/* 팔로워/팔로잉 선택 버튼 */
const selectBtn = document.querySelector(".selectBtn");
const follower = selectBtn.querySelector(".follower");
const following = selectBtn.querySelector(".following");

follower.addEventListener("click", (e)=>{
  following.classList.remove("selected");
  e.currentTarget.classList.add("selected");
});
following.addEventListener("click", (e)=>{
  follower.classList.remove("selected");
  e.currentTarget.classList.add("selected");
});

/* 멤버 더보기 버튼 */
const fMemberBtns = document.querySelectorAll(".fMember .memberBtn");
const fMemberBtnUls = document.querySelectorAll(".fMember .memberBtn ul");

fMemberBtns.forEach(fMemberBtn => {
  const fMoreBtn = fMemberBtn.querySelector(".moreBtn");
  const fMemberBtnUl = fMemberBtn.querySelector("ul");
  fMoreBtn.addEventListener("click", ()=>{

    if(fMemberBtnUl.style.display == "block"){
      fMemberBtnUl.style.display = "none";
    }
    else {
      fMemberBtnUls.forEach(a => {
        a.style.display = "none";
      })
      fMemberBtnUl.style.display = "block";
    }
  });
})