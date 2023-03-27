loadingTagsInput();

writeForm.submitBtn.addEventListener("click", ()=>{
  if(validation()){
    writeForm.method="POST";
    writeForm.action="/articles/" + writeForm.dataset.articleid +"/edit";
    writeForm.submit();
  }
})

function loadingTagsInput(){ // 로딩됐을 때 넘어온 tag들 hidden에 넣어주기
  const loadTags = document.querySelectorAll(".tagSpan");
  loadTags.forEach(loadTag => {
    tags.value += loadTag.children[0].innerText;

    // 태그 삭제 버튼 및 삭제 이벤트 추가
    let tagBtn = loadTag.querySelector('button');
    tagBtn.onclick = (ev)=>{ // 삭제 이벤트 추가
      let str = ev.currentTarget.parentElement.innerText;
      tags.value = tags.value.replace(str.substring(0, str.length-2),""); // 해당되는 태그값 hidden 에서 지우기
      ev.currentTarget.parentElement.remove(); // 태그 객체 삭제
    }
  })
}