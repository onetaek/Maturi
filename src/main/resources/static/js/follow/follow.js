
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