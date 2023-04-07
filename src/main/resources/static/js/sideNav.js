let sideNavToggleBtn = document.querySelector('.side-nav-toggle-btn');
let sideNav = document.querySelector('#sideNav');

sideNavToggleBtn.addEventListener('click',()=>{
   sideNav.classList.toggle("close");
});

if(window.innerWidth <= 1220){
   sideNav.classList.add("close");
}

addEventListener("resize", (event) => {
   if(window.innerWidth <= 1220){
      if(!sideNav.classList.contains('close')){//close가 없으면?
         sideNav.classList.add("close");
      }
   }
   if(window.innerWidth > 1221){
      if(sideNav.classList.contains('close')){//close가 없으면?
         sideNav.classList.remove("close");
      }
   }
});