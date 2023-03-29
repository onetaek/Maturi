function getIndex(ele) {
  var _i = 0;
  while ((ele = ele.previousSibling) != null) {
    _i++;
  }
  return _i;
}


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
                <button class="tag-remove-btn" onclick="removeTag(this);" type="button">
                    <ion-icon name="close-circle-outline"></ion-icon>
                </button>
            </span>`);
      tagWrap.prepend(tagBtn);
      tags.value += tagInput.value; // 백에 넘길값(tags)에 추가
      tagInput.value = "";

    //   tagBtn.addEventListener('click',(ev)=>{
    //     console.log('닫기클릭');
    //     console.log(ev.target);
    //     console.log(ev.currentTarget);
    //     console.log(ev.currentTarget.parentElement);
    //     console.log(ev.currentTarget.parentElement.firstChild);
    //     console.log(ev.currentTarget.parentElement.firstChild.innerText);
    //     console.log('몇번째니?',$('.tag-box').index(this));
    //     console.log("index찾기");
    //     console.log(getIndex(ev));
    //     console.log(getIndex(ev.currentTarget.parentElement));
    //     console.log(getIndex(ev.currentTarget.parentElement.firstChild));
    //       let str = ev.currentTarget.parentElement.firstChild.innerText;
    //       ev.currentTarget.parentElement.firstChild.remove(); // 태그 객체 삭제
    //       tags.value = tags.value.replace(str.substring(0, str.length-1),""); // 해당되는 태그값 hidden 에서 지우기
    //   });
    //   tagInput.focus();
    }
  }
})

/* 업로드 */
function validation(){
  let imageInput = document.querySelector('.old-files');
  let files = imageInput.files;
  console.log("files",files);
  console.log("길이",files.files === undefined);
  let loadImgItem = document.querySelectorAll('.load-img-item');
  if(loadImgItem.length === 0 && files.files === undefined){ // 이미지 1개 이상
    alert("리뷰 작성을 하기 위해서는 하나 이상의 사진이 필요합니다!");
    return false;
  }
  return true; // 유효성검사 통과시
}

/* 태그 삭제 */
function removeTag(obj){
  console.log("obj",obj);
  console.log("삭제하기전의 input안에 있는 값",tags.value);
  //현재 버튼(obj)의 상위 요소 중 가장 가까운 div 요소를 찾음
  //div는 선택한 파일 목록을 보여주는 부분
  const tagWrap = $(obj).closest('.tagWrap');
  console.log("태그 목록을 보여주는 부분",tagWrap);
  const tagBox = $(obj).closest('.tag-box');
  console.log("삭제할 요소",tagBox);

  //div에서 'button' 태그 중 현재 버튼(obj)의 index가 몇 번째인지 확인
  //0부터 시작
  const index = tagWrap.find('.tag-remove-btn').index(obj);
  console.log("몇번째 버튼인지",index);
  let tagValues = document.querySelectorAll('.tag-box > span');
  let tagValue = tagValues[index].innerText;
  console.log("실제로 저장할 tag값",tagValue);
  tags.value = tags.value.replace(tagValue.substring(0, tagValue.length-1),""); // 해당되는 태그값 hidden 에서 지우기
  console.log("input안에 있는 값",tags.value);
  tagBox.remove();
}

//textarea 동적 크기조절
const textarea = document.querySelector("textarea");
textarea.addEventListener("keyup", e =>{
  textarea.style.height = "63px";
  let scHeight = e.target.scrollHeight;
  textarea.style.height = `${scHeight}px`;
});

