writeForm.submitBtn.addEventListener("click", ()=>{
  if(newWriteFormValidation()){

    let imageSize = '';
    let elements = document.querySelectorAll('.img-item');
    console.log("elements",elements);
    elements.forEach((element)=>{
      let fileSize = element.dataset.size;
      console.log("size",fileSize);
      imageSize += fileSize + ",";
    })
    console.log("imageSize",imageSize);

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

function newWriteFormValidation(){
  // let imageInput = document.querySelector('.old-files').files;
  // let files = imageInput.files;
  // console.log("files",files);
  // console.log("길이",files.files === undefined);
  // console.log("imageInput",imageInput);
  // console.log("imageInput length",imageInput.length);'
  console.log("진짤루",document.querySelector('.old-files').files);
  console.log("진짤루",document.querySelector('#old-files').files);


  let element = document.querySelector('#old-files');


  // console.log("oldFiles",oldFiles);
  // console.log("oldFiles의 길이",oldFiles.length);

  if(document.querySelector('.old-files').files.length===0){ // 이미지 1개 이상
    alert("리뷰 작성을 하기 위해서는 하나 이상의 사진이 필요합니다!");
    return false;
  }
  return true; // 유효성검사 통과시
}
