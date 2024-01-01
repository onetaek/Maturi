/* 슬라이드 */
const bImg = document.querySelector(".bImg"); // ul
const imgList = bImg.querySelectorAll("li"); // li
const bImgAll = bImg.querySelectorAll("img"); // img
bImgAll.forEach(img => {
  img.addEventListener("mousedown", (e)=>{
    e.preventDefault();
  })
});

// 페이지 로드시 이미지 개수에 춰 width 조정
window.addEventListener("load", function(){
  bImg.style.width = `${imgList.length * 100}%`;
  bImg.style.visibility = "visible";//이미지가 로드된 후 보여짐
});

let imgNum = 0; // 현재 이미지의 index 번호
let slideState = 1; // 애니매이션 중일 때 = 0
function imgMove(){
  bImg.style.transition = '0.5s';
  bImg.style.marginLeft = `-${imgNum * 100}%`;
  setTimeout(function(){
    bImg.style.transition = '0s';
  }, 600);
}

// 드래그 이벤트
let mouseDown = 0;
let mouseUp = 0;
let mouseDrag = false; // 드래그 중에 true
bImg.addEventListener("mousedown", function(e){ // 클릭 시작
  mouseDrag = true;
  mouseDown = e.pageX;
});
bImg.addEventListener("mouseup", function(e){ // 클릭 끝
  mouseUp = e.pageX;
  // 현재 imgList의 너비의 30%
  let imgListPer30 = 30*(imgList[0].clientWidth/100);
  if(mouseUp < mouseDown - imgListPer30){ // 다음 슬라이드
    if(imgNum < imgList.length-1){
      imgNum ++;
    }
  }
  else if(mouseUp > mouseDown + imgListPer30){ // 이전 슬라이드
    if(imgNum > 0){
      imgNum--;
    }
  }
  else { // 드래그 폭 좁을 경우 현재 슬라이드로 되돌아가기
  }
  imgMove();
  mouseDrag = false;
});
bImg.addEventListener("mousemove", function(e){ // 클링중
  if(mouseDrag === true){
    let dragGap = mouseDown - e.pageX;
    let dragGapPer = 100*(dragGap/imgList[0].clientWidth);
    let marginValue = (imgNum*100)+dragGapPer;
    if(marginValue >= 0 && marginValue <= 400){
      e.currentTarget.style.marginLeft = `-${(imgNum*100)+dragGapPer}%`;
    }
  }
});

/* 좋아요 클릭 */
const articleWrap = document.getElementById("review-container");
const likeAndTag = document.querySelector(".likeAndTag");
const likeBtn = likeAndTag.querySelector(".likeBtn");
const likeNum = likeAndTag.querySelector(".likeNum");
likeBtn.addEventListener("click", ()=>{
  const url = "/api/articles/"+articleWrap.dataset.articleid+"/like";
  fetch(url, {
    method: "post",
    headers: {
      "Content-type": "application/json"
    }
  }).then((response) => response.json())
    .then((data) => {
    if(data.isLiked == 1){ // 좋아요!
      likeBtn.classList.add("isLikedArticle");
    } else { // 좋아요 취소!
      likeBtn.classList.remove("isLikedArticle");
    }
    likeNum.innerText = data.likeNum;
  })
});

/* textarea높이 조절 */
function adjustTextareaHeight(textarea) {
  textarea.style.height = "auto";
  textarea.style.height = textarea.scrollHeight + "px";
}

// 페이지 로딩 시 textarea 높이 조절
document.addEventListener("DOMContentLoaded", function () {
  let textarea = document.querySelector('.bContent');
  adjustTextareaHeight(textarea);
});

// textarea의 내용이 변경될 때마다 높이 조절
document.querySelector('.bContent').addEventListener("input", function () {
  adjustTextareaHeight(this);
});

// /* 게시글 삭제 */
// const deleteForm = document.deleteForm;
// const deleteBtn = document.querySelector(".deleteArticle");
// deleteBtn.addEventListener("click", ()=>{
//   deleteForm.submit();
// })