const editProfileForm = document.editProfileForm;

/* 파일 선택과 버튼 연결 */
// 커버 이미지
const coverImgWrap = document.getElementById("coverImg");
const coverImgBtn = coverImgWrap.querySelector(".ImgModifyBtn");
const coverImg = editProfileForm.coverImg;
coverImgBtn.addEventListener("click", ()=>{
  coverImg.click();
})
// 프로필 이미지
const myPageInfoWrap = document.getElementById("myPageInfo");
const profileImgBtn = myPageInfoWrap.querySelector(".ImgModifyBtn");
const profileImg = editProfileForm.profileImg;
profileImgBtn.addEventListener("click", ()=>{
  profileImg.click();
})

/* 사진 변경 */
// 기본값 저장
const coverImgSrc = coverImgWrap.querySelector(".coverImgTag").getAttribute("src");
const profileImgSrc = myPageInfoWrap.querySelector(".profileImgTag").getAttribute("src");

coverImg.addEventListener("change", (e)=>{
  let file = e.target.files;
  let reader = new FileReader();
  reader.onload = function (ee){
    coverImgWrap.querySelector(".coverImgTag").src = ee.target.result;
  }
  reader.readAsDataURL(file[0]);
  coverImgWrap.querySelector(".coverImgTag").classList.remove("nullImg");
});

profileImg.addEventListener("change", (e)=>{
  let file = e.target.files;
  let reader = new FileReader();
  reader.onload = function (ee){
    myPageInfoWrap.querySelector(".profileImgTag").src = ee.target.result;
  }
  reader.readAsDataURL(file[0]);
  myPageInfoWrap.querySelector(".profileImgTag").classList.remove("nullImg");
});


/* 수정하기 버튼 */
editProfileForm.editSubmitBtn.addEventListener("click", ()=>{
  if(editProfileValidate()){ // 유효성검사
    Swal.fire({
      title: "프로필을 수정하시겠습니까?",
      icon: "question",
      showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
      confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
      cancelButtonColor: '#6e7881', // cancel 버튼 색깔 지정
      confirmButtonText: '프로필 수정', // confirm 버튼 텍스트 지정
      cancelButtonText: '취소', // cancel 버튼 텍스트 지정
      reverseButtons: false, // 버튼 순서 거꾸로
      // background-color: #6e7881
    }).then((result) => {
      if (result.isConfirmed) {
        editProfileForm.action = `/members/${memberId}/edit`;
        editProfileForm.submit();
      }
    });

  }
});

/* 취소 버튼 */
editProfileForm.editCancelBtn.addEventListener("click", ()=>{
  Swal.fire({
    title: "프로필 수정을 취소하시겠습니까?",
    text: "마이페이지로 돌아갑니다",
    icon: "question",
    showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
    confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
    cancelButtonColor: '#d33', // cancel 버튼 색깔 지정
    confirmButtonText: '돌아가기', // confirm 버튼 텍스트 지정
    cancelButtonText: '취소', // cancel 버튼 텍스트 지정
    reverseButtons: false, // 버튼 순서 거꾸로
    // background-color: #6e7881
  }).then((result) => {
    if (result.isConfirmed) {
      location.href = `/members/${memberId}`;
    }
  });
});

/* 유효성 검사 폼 */
const editNickName = editProfileForm.nickName;
const editName = editProfileForm.name;

let regExpNickName = /^@+[a-zA-Z0-9-_.]{5,30}$/;
let regExpName = /[a-z|A-Z|가-힣]{2,12}$/;
function editProfileValidate(){
  if(!regExpNickName.test(editNickName.value)){
    Swal.fire({
      title: "닉네임은 @로 시작하며 [영문], [숫자], [-], [_], [.]를 조합하여 만들 수 있습니다. (5자 ~ 30자)",
      icon: 'info',
      confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
    })
    editNickName.focus();
    return false;
  }
  else if(!nickNameCheck){
    Swal.fire({
      title: "닉네임 중복 검사를 먼저 진행해주세요!",
      icon: 'warning',
      confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
    })
    editNickName.focus();
    return false;
  }
  else if(!regExpName.test(editName.value)){
    Swal.fire({
      title: "이름은 [영문], [한글]을 조합하여 만들 수 있습니다. (2자 ~ 12자)",
      icon: 'info',
      confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
    })
    editName.focus();
    return false;
  }
  else return true;
}

/* 닉네임 중복 검사 */
let nickNameCheck = true; // 닉네임 중복 검사 통과 여부 (초기값 : 닉넴수정x -> true)
const nickNameDuplCheck = editProfileForm.querySelector(".nickNameDuplCheck");
nickNameDuplCheck.addEventListener("click", ()=>{
  if(!regExpNickName.test(editNickName.value)){
    Swal.fire({
      title: "닉네임은 @로 시작하며 [영문], [숫자], [-], [_], [.]를 조합하여 만들 수 있습니다. (5자 ~ 30자)",
      icon: 'info',
      confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
    })
    editNickName.focus();
  }
  else {
    const url = "/api/members/nickNameCheck";
    fetch(url, {
      method: "POST",
      body: JSON.stringify({
        nickName: editNickName.value
      }),
      headers: {
        "Content-type": "application/json"
      }
    }).then(response => {
      if(response.ok){
        Swal.fire({
          title: "중복 닉네임이 존재합니다!",
          icon: 'warning',
          confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
        })
        editNickName.focus();
      } else {
        Swal.fire({
          title: "사용 가능한 닉네임입니다!",
          icon: 'success',
          confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
        })
        nickNameCheck = true;
      }
    })
  }
});

// 닉네임 바꿀 때마다 중복검사 재실행하도록
editNickName.addEventListener("keyup", ()=>{
  if(nickNameCheck){
    nickNameCheck = false;
  }
})

//textarea 동적 크기조절
const textarea = document.querySelector("textarea");
textarea.addEventListener("keyup", e =>{
  textarea.style.height = "63px";
  let scHeight = e.target.scrollHeight;
  textarea.style.height = `${scHeight}px`;
});