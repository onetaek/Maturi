writeForm.submitBtn.addEventListener("click", ()=>{
  if(editeWriteFormValidation()){
    let oldImages = document.querySelectorAll('.load-img-item');
    let oldImagesString = "";
    oldImages.forEach((oldImage)=>{
      let oldImageString = oldImage.dataset.image;
      oldImagesString += oldImageString+",";
    });

    //2131232k1j32ji1l.png,329u13j2k12.png -> 이런식으로 원래 있던 파일을들 전송
    let imageHiddenField = document.createElement("input");
    imageHiddenField.setAttribute("type", "hidden");
    imageHiddenField.setAttribute("name", "oldImage");
    imageHiddenField.setAttribute("value", oldImagesString);
    writeForm.appendChild(imageHiddenField);

    //2132,14512,342412, -> 이런식으로 크기를 string으로 변환
    let imageSize = '';
    let elements = document.querySelectorAll('.img-item');
    elements.forEach((element)=>{
      let fileSize = element.dataset.size;
      imageSize += fileSize + ",";
    })

    let sizeHiddenField = document.createElement("input");
    sizeHiddenField.setAttribute("type", "hidden");
    sizeHiddenField.setAttribute("name", "imageSize");
    sizeHiddenField.setAttribute("value", imageSize);
    writeForm.appendChild(sizeHiddenField);

    writeForm.method="POST";
    writeForm.action="/articles/" + writeForm.dataset.articleid +"/edit";
    writeForm.submit();
  }
})

function loadingTagsInput(){ // 로딩됐을 때 넘어온 tag들 hidden에 넣어주기
  const loadTags = document.querySelectorAll(".tag-box");
  loadTags.forEach(loadTag => {
    tags.value += loadTag.children[0].innerText;
  })
}

/* 업로드 */
function editeWriteFormValidation(){


  let loadImgItem = document.querySelectorAll('.load-img-item');
  if(loadImgItem.length === 0 && document.querySelector('.old-files').files.length === 0){ // 이미지 1개 이상
    Swal.fire({
      title: "리뷰 작성을 하기 위해서는 하나 이상의 사진이 필요합니다",
      icon: 'info',
      confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
    })
    return false;
  }
  return true; // 유효성검사 통과시
}

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


loadingTagsInput();