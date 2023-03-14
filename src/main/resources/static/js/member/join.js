const joinForm = document.joinForm;
const submitBtn = joinForm.submitBtn;

submitBtn.addEventListener("click", ()=>{
  joinForm.method = "post";
  joinForm.action = "/member/join";
  joinForm.submit();
})
