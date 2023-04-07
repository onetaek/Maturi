/* 프로필 편집 버튼 클릭 */
const editMyPage = document.querySelector(".editMyPage");
if(editMyPage != null){
    editMyPage.addEventListener("click", (e)=>{
        e.preventDefault();
        location.href = `/members/${memberId}/edit`;
    })
}

//게시글의 유저 팔로잉
function myPagefollowing(memberId,myPageMemberId,myPageMemberNickName){
    console.log("팔로잉 시작");
    console.log("게시글의 id",memberId);
    console.log("게시글의 memberId",myPageMemberId);
    if(confirm(`${myPageMemberNickName}님을(를) 팔로잉 하시겠습니까?`)){
        fetch(`/api/members/${memberId}/following`,{
            method:"POST",
            headers:{
                "Content-Type":"application/json",
            },
            body:JSON.stringify({
                "followingMemberId":myPageMemberId
            })
        }).then((response) =>{
            console.log("response의 상태코드",response.status);
            if(response.status === 226){
                alert(`${myPageMemberNickName}님은(는) 이미 팔로잉하고있는 유저입니다.`);
            }
            if(response.status === 400){
                alert("잘못된 접근 방법입니다");
            }
            if(response.ok){
                alert(`${myPageMemberNickName}님을(를) 팔로잉하는데 성공했습니다.`);
                window.location.href=`/members/${myPageMemberId}`;
            }
        })
    }
}

//게시글의 유저 팔로잉
function myPagefollowCancel(memberId,myPageMemberId,myPageMemberNickName){
    console.log("팔로잉취소 시작");
    console.log("게시글의 memberId",myPageMemberId);
    if(confirm(`${myPageMemberNickName}님을(를) 팔로우 취소 하시겠습니까?`)){
        fetch(`/api/members/${memberId}/following`,{
            method:"DELETE",
            headers:{
                "Content-Type":"application/json",
            },
            body:JSON.stringify({
                "followingMemberId":myPageMemberId
            })
        }).then((response) =>{
            console.log("response의 상태코드",response.status);
            if(response.ok){
                alert(`${myPageMemberNickName}님을(를) 팔로우 취소하였습니다!`);
                window.location.href=`/members/${myPageMemberId}`;
            }else{
                alert(`${myPageMemberNickName}님을(를) 팔로우 취소하는데 실패하였습니다`);
            }
        })
    }
}

//프로필, 이름, 닉네임을 클릭하면 해당 유저의 마이페이지로 이동
let element1 = document.querySelector('#myPageInfo .mProfileImg');
let element2 = document.querySelector('.mNickName');
let element3 = document.querySelector('.mName');
let element4 = document.querySelector('.mProfile');
let elementArr = [element1,element2,element3,element4];
elementArr.forEach((element)=>{
    element.style.cursor='pointer';
    element.addEventListener('click',()=>{
       window.location.href=`/members/${myPageMemberId}`;
    });
})