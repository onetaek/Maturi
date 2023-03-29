writeForm.submitBtn.addEventListener("click", ()=>{
  if(editeWriteFormValidation()){
    let oldImages = document.querySelectorAll('.load-img-item');
    let oldImagesString = "";
    oldImages.forEach((oldImage)=>{
      let oldImageString = oldImage.dataset.image;
      oldImagesString += oldImageString+",";
    });

    //2131232k1j32ji1l.png,329u13j2k12.png -> 이런식으로 원래 있던 파일을들 전송
    console.log("기존 이미지 String으로 변환",oldImagesString);
    let imageHiddenField = document.createElement("input");
    imageHiddenField.setAttribute("type", "hidden");
    imageHiddenField.setAttribute("name", "oldImage");
    imageHiddenField.setAttribute("value", oldImagesString);
    writeForm.appendChild(imageHiddenField);

    //2132,14512,342412, -> 이런식으로 크기를 string으로 변환
    let imageSize = '';
    let elements = document.querySelectorAll('.img-item');
    console.log("elements",elements);
    elements.forEach((element)=>{
      let fileSize = element.dataset.size;
      console.log("size",fileSize);
      imageSize += fileSize + ",";
    })
    console.log("imageSize",imageSize);

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
    console.log("로딩될때 tags에들어간 값들",tags.value);
  })
}

/* 업로드 */
function editeWriteFormValidation(){

  console.log("진짤루",document.querySelector('.old-files').files);

  let loadImgItem = document.querySelectorAll('.load-img-item');
  console.log("loadImgItem",loadImgItem.length);
  console.log("document.querySelector('.old-files').files",document.querySelector('.old-files').files.length);
  if(loadImgItem.length === 0 && document.querySelector('.old-files').files.length === 0){ // 이미지 1개 이상
    alert("리뷰 작성을 하기 위해서는 하나 이상의 사진이 필요합니다!");
    return false;
  }
  return true; // 유효성검사 통과시
}

loadingTagsInput();