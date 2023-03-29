loadingTagsInput();

writeForm.submitBtn.addEventListener("click", ()=>{
  if(validation()){
    let oldImages = document.querySelectorAll('.load-img-item');
    let oldImagesString = "";
    oldImages.forEach((oldImage)=>{
      let oldImageString = oldImage.dataset.image;
      oldImagesString += oldImageString+",";
    });

    console.log("기존 이미지 String으로 변환",oldImagesString);
    let hiddenField = document.createElement("input");
    hiddenField.setAttribute("type", "hidden");
    hiddenField.setAttribute("name", "oldImages");
    hiddenField.setAttribute("value", oldImagesString);
    writeForm.appendChild(hiddenField);

    writeForm.method="POST";
    writeForm.action="/articles/" + writeForm.dataset.articleid +"/edit";
    // writeForm.submit();
  }
})

function loadingTagsInput(){ // 로딩됐을 때 넘어온 tag들 hidden에 넣어주기
  const loadTags = document.querySelectorAll(".tag-box");
  loadTags.forEach(loadTag => {
    tags.value += loadTag.children[0].innerText;
    console.log("로딩될때 tags에들어간 값들",tags.value);
  })
}