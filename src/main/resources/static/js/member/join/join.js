const joinForm = document.joinForm;
const submitBtn = joinForm.submitBtn;

// submit 하기
submitBtn.addEventListener("click", ()=>{
  // post 타입으로 submit
  joinForm.method = "post";
  joinForm.action = "/member/join";
  joinForm.submit();
});

// 정규식
// let reqExpPasswd = /^(?=.*[a-z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}/;
