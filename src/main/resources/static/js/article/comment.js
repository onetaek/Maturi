
const cMemberBtns = document.querySelectorAll(".cMemberBtn");

const cMemberBtnUls = document.querySelectorAll(".cMemberBtn ul");
// const cMemberBtnUl = cMemberBtn.querySelector("ul");

cMemberBtns.forEach(cMemberBtn => {
  const cMoreBtn = cMemberBtn.querySelector(".moreBtn");
  const cMemberBtnUl = cMemberBtn.querySelector("ul");
  cMoreBtn.addEventListener("click", ()=>{

    if(cMemberBtnUl.style.display == "block"){
      cMemberBtnUl.style.display = "none";
    }
    else {
      cMemberBtnUls.forEach(a => {
        a.style.display = "none";
      })
      cMemberBtnUl.style.display = "block";
    }
  });
})
