/* 더보기 버튼 */
const ellipsisBtn = document.querySelectorAll(".ellipsis-btn");
const ellipsisContent = document.querySelectorAll(".ellipsis-content");

for(let i = 0 ; i < ellipsisBtn.length; i++){
  ellipsisBtn[i].addEventListener("click",function(){
    ellipsisContent[i].classList.toggle("active");
  });
}
