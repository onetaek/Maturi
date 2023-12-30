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

window.addEventListener("resize", adjustMargin);

function adjustMargin() {
   var sideNav = document.getElementById("sideNav");
   var gnb = document.getElementById("gnb");
   var windowHeight = window.innerHeight;

   // Check if sidebar height is greater than window height
   if (sideNav.clientHeight > windowHeight) {
      gnb.style.marginBottom = '50px';
   } else {
      // Reset margin if sidebar height is smaller than window height
      gnb.style.marginBottom = '100px';
   }
}

// Initial adjustment on page load
adjustMargin();