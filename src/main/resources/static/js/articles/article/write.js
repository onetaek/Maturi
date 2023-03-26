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
      // 태그 span 추가
      let tagSpan = document.createElement("span");
      tagSpan.innerText = tagInput.value;
      tagSpan.classList.add("tagSpan");
      tagWrap.appendChild(tagSpan);

      // 태그 삭제 버튼 및 삭제 이벤트 추가
      let tagBtn = document.createElement('button');
      tagBtn.setAttribute('type', 'button');
      tagBtn.innerText = "x";
      tagSpan.appendChild(tagBtn);
      tagBtn.onclick = (ev)=>{ // 삭제 이벤트 추가
        let str = ev.currentTarget.parentElement.innerText;
        ev.currentTarget.parentElement.remove(); // 태그 객체 삭제
        tags.value = tags.value.replace(str.substring(0, str.length-1),""); // 해당되는 태그값 hidden 에서 지우기
      }

      tags.value += tagInput.value; // 백에 넘길값(tags)에 추가

      tagInput.value = "";
      tagInput.focus();
    }
  }
})

/* 이미지 유효성 검사 - 이미지 적어도 1개 이상, 파일 확장자 검사 */
const imageInput = writeForm.image;
imageInput.addEventListener("change", ()=>{ // 테스트
  let files = imageInput.files;
  let fileArr = Array.prototype.slice.call(files);
  console.log(files);
  fileArr.forEach(file => {
    let extension = file.name.slice(file.name.lastIndexOf(".")+1).toLowerCase();
    // jpg, png, gif, bmp
    if(extension != "jpg" && extension != "png" &&
        extension != "gif" && extension != "bmp"){
      alert("이미지 파일은 (jpg, png, gif, bmp) 형식만 사용 가능합니다.");
      imageInput.value = "";
      return;
    }
  })
})

/* 업로드 */
function validation(){
  let fileArr = Array.prototype.slice.call(imageInput.files);
  console.log(fileArr);
  console.log(fileArr.length);

  if(fileArr.length == 0){ // 이미지 1개 이상
    alert("리뷰 작성을 하기 위해서는 하나 이상의 사진이 필요합니다!");
    return false;
  }

  return true; // 유효성검사 통과시
}
writeForm.submitBtn.addEventListener("click", ()=>{
  if(validation()){
    writeForm.method="POST";
    writeForm.action="/articles/new";
    writeForm.submit();
  }
})