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
      let template = `<span class="tagSpan">${tagInput.value}</span>`;
      tags.value += tagInput.value; // 백에 넘길값(tags)에 추가
      tagWrap.insertAdjacentHTML("beforeend", template);
      tagInput.value = "";
      tagInput.focus();
    }
  }
})

/* 업로드 */
function validation(){
  return true; // 유효성검사 통과시
}
writeForm.submitBtn.addEventListener("click", ()=>{
  if(validation){
    writeForm.submit();
  }
})