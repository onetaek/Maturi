function newWriteFormValidation(){

  console.log("진짤루",document.querySelector('.old-files').files);
  console.log("진짤루",document.querySelector('#old-files').files);

  if(oldFiles.files.length===0){ // 이미지 1개 이상
    alert("리뷰 작성을 하기 위해서는 하나 이상의 사진이 필요합니다!");
    return false;
  }
  return true; // 유효성검사 통과시
}

//글작성 버튼 클릭
writeForm.submitBtn.addEventListener("click", ()=>{
  if(newWriteFormValidation()){

    //이미지 크기 변환 작업
    let imageSize = '';
    let elements = document.querySelectorAll('.img-item');
    console.log("elements",elements);
    elements.forEach((element)=>{
      let fileSize = element.dataset.size;
      console.log("size",fileSize);
      imageSize += fileSize + ",";
    })
    console.log("imageSize",imageSize);

    //이미지 크기 값 input hidden값 생성후 appendChild
    let hiddenField = document.createElement("input");
    hiddenField.setAttribute("type", "hidden");
    hiddenField.setAttribute("name", "imageSize");
    hiddenField.setAttribute("value", imageSize);
    writeForm.appendChild(hiddenField);
    writeForm.method="POST";
    writeForm.action="/articles/new";
    writeForm.submit();
  }
})

