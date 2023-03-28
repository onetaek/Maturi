const writeForm = document.writeForm;

/* 태그 */
const tagInput = writeForm.tag;
const tags = writeForm.tags;
const tagWrap = document.querySelector(".tagWrap");

// 첫자#, 한글/영문/숫자/_ 만 포함
let regExpTag = /^#+[가-힣|a-z|A-Z|0-9|_]/;
tagInput.addEventListener("keyup", ()=>{
  if( window.event.keyCode === 13){
    if(!regExpTag.test(tagInput.value)){ // 유효성검사 통과 x
      tagInput.value = "";
      alert("#태그 형식으로 작성해주세요 ..");
      tagInput.focus();
    }
    else if(tags.value.includes(tagInput.value)){
      alert("이미 추가된 태그입니다!");
      tagInput.focus();
    }
    else { // 유효성검사 통과
      // 태그 삭제 버튼 및 삭제 이벤트 추가
      let tagBtn = myCreateElement(`
            <span class="tag-box">
                <span>${tagInput.value}</span>
                <button class="tag-remove-btn" type="button">
                    <ion-icon name="close-circle-outline"></ion-icon>
                </button>
            </span>`);
      tagBtn.addEventListener('click',(ev)=>{
        console.log('닫기클릭');
          let str = ev.currentTarget.parentElement.innerText;
          ev.currentTarget.parentElement.remove(); // 태그 객체 삭제
          tags.value = tags.value.replace(str.substring(0, str.length-1),""); // 해당되는 태그값 hidden 에서 지우기
      });
      tagWrap.prepend(tagBtn);
      tags.value += tagInput.value; // 백에 넘길값(tags)에 추가

      tagInput.value = "";
      tagInput.focus();
    }
  }
})

/* 업로드 */
function validation(){
  let imageInput = document.querySelector('.old-files');
  let files = imageInput.files;

  if(files.files.length === 0){ // 이미지 1개 이상
    alert("리뷰 작성을 하기 위해서는 하나 이상의 사진이 필요합니다!");
    return false;
  }
  return true; // 유효성검사 통과시
}
//textarea 동적 크기조절
const textarea = document.querySelector("textarea");
textarea.addEventListener("keyup", e =>{
  textarea.style.height = "63px";
  let scHeight = e.target.scrollHeight;
  textarea.style.height = `${scHeight}px`;
});