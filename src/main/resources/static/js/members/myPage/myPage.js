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
    Swal.fire({
        title: `${myPageMemberNickName}님을(를) 팔로잉 하시겠습니까?`,
        icon: "question",
        showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
        cancelButtonColor: '#6e7881', // cancel 버튼 색깔 지정
        confirmButtonText: '팔로잉하기', // confirm 버튼 텍스트 지정
        cancelButtonText: '취소', // cancel 버튼 텍스트 지정
        reverseButtons: false, // 버튼 순서 거꾸로
    }).then((result)=>{
        if(result.isConfirmed){
            fetch(`/api/members/${memberId}/following`,{
                method:"POST",
                headers:{
                    "Content-Type":"application/json",
                },
                body:JSON.stringify({
                    "followingMemberId":myPageMemberId
                })
            }).then((response) =>{
                if(response.status === 226){
                    Swal.fire({
                        icon:'error',
                        title:`${myPageMemberNickName}님은(는) 이미 팔로잉하고있는 유저입니다.`
                    })
                }
                if(response.status === 400){
                    Swal.fire({
                        icon:'error',
                        title:`잘못된 접근 방법입니다`
                    })
                }
                if(response.ok){
                    Swal.fire({
                        icon:'success',
                        title:`${myPageMemberNickName}님을(를) 팔로잉하는데 성공했습니다.`
                    }).then(function(){
                        window.location.href=`/members/${myPageMemberId}`;
                    })
                }
            })
        }
    })
}

//게시글의 유저 팔로잉
function myPagefollowCancel(memberId,myPageMemberId,myPageMemberNickName){
    Swal.fire({
        title: `${myPageMemberNickName}님을(를) 팔로잉 취소 하시겠습니까?`,
        icon: "question",
        showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
        cancelButtonColor: '#6e7881', // cancel 버튼 색깔 지정
        confirmButtonText: '팔로잉취소하기', // confirm 버튼 텍스트 지정
        cancelButtonText: '취소', // cancel 버튼 텍스트 지정
        reverseButtons: false, // 버튼 순서 거꾸로
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/api/members/${memberId}/following`,{
                method:"DELETE",
                headers:{
                    "Content-Type":"application/json",
                },
                body:JSON.stringify({
                    "followingMemberId":myPageMemberId
                })
            }).then((response) =>{
                if(response.ok){
                    Swal.fire({
                        icon:'success',
                        title:`${myPageMemberNickName}님을(를) 팔로우 취소하였습니다!`
                    }).then(function(){
                        window.location.href=`/members/${myPageMemberId}`;
                    })

                }else{
                    Swal.fire({
                        icon:'error',
                        title:`${myPageMemberNickName}님을(를) 팔로우 취소하는데 실패하였습니다`
                    })
                }
            })
        }
    });

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