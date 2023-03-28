writeForm.submitBtn.addEventListener("click", ()=>{
  if(validation()){
    writeForm.method="POST";
    writeForm.action="/articles/new";
    writeForm.submit();
  }
})