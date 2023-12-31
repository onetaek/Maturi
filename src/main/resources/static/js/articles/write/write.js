

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
      Swal.fire({
        title: "#태그 형식으로 작성해주세요 ..",
        icon: 'success',
        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
      })
      tagInput.focus();
    }
    else if(tags.value.includes(tagInput.value)){
      Swal.fire({
        title: "이미 추가된 태그입니다!",
        icon: 'success',
        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
      })
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
    }
  }
})

/* 태그 삭제 */
function removeTag(obj){
  //현재 버튼(obj)의 상위 요소 중 가장 가까운 div 요소를 찾음
  //div는 선택한 파일 목록을 보여주는 부분
  const tagWrap = $(obj).closest('.tagWrap');
  const tagBox = $(obj).closest('.tag-box');

  //div에서 'button' 태그 중 현재 버튼(obj)의 index가 몇 번째인지 확인
  //0부터 시작
  const index = tagWrap.find('.tag-remove-btn').index(obj);
  let tagValues = document.querySelectorAll('.tag-box > span');
  let tagValue = tagValues[index].innerText;
  tags.value = tags.value.replace(tagValue.substring(0, tagValue.length-1),""); // 해당되는 태그값 hidden 에서 지우기
  tagBox.remove();
}

//textarea 동적 크기조절
const textarea = document.querySelector("textarea");
textarea.addEventListener("keyup", e =>{
  textarea.style.height = "63px";
  let scHeight = e.target.scrollHeight;
  textarea.style.height = `${scHeight}px`;
});

