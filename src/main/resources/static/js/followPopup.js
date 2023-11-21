//팝업 클릭, 닫기 버튼
function followPopupToggle(){
    let blur = document.querySelector('#wrap');
    blur.classList.toggle('active');
    let popup = document.getElementById('followPopup');
    popup.classList.toggle('active');
    if (isModal === 0){
        scrollStop();
    }else{
        scrollPlay();
    }
}
//팔로우 팔로워 버튼 horizontalUnderLine 이벤트
let horizontalUnderLine = document.querySelector("#horizontal-under-line");
let followMenu = document.querySelectorAll(".follow-container");
followMenu.forEach((menu) =>
    menu.addEventListener("click",(e) =>
    horizontalIndicator(e))
);

//메뉴버튼 아래 따라다니는 선들
function horizontalIndicator(e){
    horizontalUnderLine.style.left = e.currentTarget.offsetLeft + "px";
    horizontalUnderLine.style.width = e.currentTarget.offsetWidth + "px";
    horizontalUnderLine.style.top =
        e.currentTarget.offsetTop + e.currentTarget.offsetHeight - 4 + "px";
}

//팔로우 팔로워 목록 가져오는 작업
let followRadioValue;
let keywordFollowValue;
function settingFollowCond(){
    if($('#follower').is(':checked')){
        followRadioValue = document.querySelector('#follower').value;
    }
    if($('#following').is(':checked')){
        followRadioValue = document.querySelector('#following').value;
    }
    keywordFollowValue = document.querySelector('.follow-search-input').value;
}

<!--팔로우, 팔로워 목록을 가져오는 함수-->
function getFollows(){
    settingFollowCond();
    fetch(`/api/members/${memberId}/follows?follow=${followRadioValue}&keyword=${keywordFollowValue}`)
        .then((response) => {
            if(response.status === 400){
                Swal.fire({
                    title: '잘못된 접근 방법입니다',
                    icon: 'error',
                    confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                })
                return false;
            }
            return response.json()
        })
        .then((members) => {
            let followUl = document.querySelector('#follow-list');
            let html = ``;
            members.forEach((member) => {
                html += `<li class="follow-item">
                    <div class="follow-item-info" onclick="location.href='/members/${member.id}';" style="cursor: pointer;">`
                if(member.profileImg == null || member.profileImg === ""){
                    html += `<img src="/img/profileImg/default_profile.png" alt="프로필 이미지">`
                }else if(member.profileImg.includes("http")){
                    html += `<img src="${member.profileImg}" alt="프로필 이미지">`
                }else{
                    html += `<img src="/upload/${member.profileImg}" alt="프로필 이미지">`
                }
                html+=`<div class="follow-item-profile">
                            <p>
                                <span class="nickName">${member.nickName}</span>
                                <span class="name">(${member.name})</span>
                            </p>
                            <p class="profile-message">${member.profile}</p>
                        </div>
                    </div>`
                if(followRadioValue === "follower"){
                    if(member.followingMember === true){
                    html += `<button class="follow-btn" 
                            style="cursor: auto;background-color:#62CDFF;">맞팔유저</button>`
                    }else{
                    html += `<button class="follow-btn" 
                    onclick="popupFollowing(${member.id},'${member.nickName}')">맞팔하기</button>`;
                    }
                }else if(followRadioValue === "following"){
                    html += `<button class="follow-btn" 
                    onclick="popupFollowCancel(${member.id},'${member.nickName}')">팔로잉 취소</button>`;
                }
                `</li>`;
            })//forEach끝
            followUl.innerHTML = html;
        })

}

//팝업창에서 팔로우 취소
function popupFollowCancel(followingMemberId,followingMemberNickName){
    console.log("팝업 팔로우 취소 시작");
    Swal.fire({
        title: `${followingMemberNickName}님을(를) 팔로잉 취소 하시겠습니까?`,
        icon: "question",
        showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
        confirmButtonColor: '#d33', // confrim 버튼 색깔 지정
        cancelButtonColor: '#6e7881', // cancel 버튼 색깔 지정
        confirmButtonText: '팔로잉취소', // confirm 버튼 텍스트 지정
        cancelButtonText: '취소', // cancel 버튼 텍스트 지정
        reverseButtons: false, // 버튼 순서 거꾸로
        // background-color: #6e7881
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/api/members/${memberId}/following`,{
                method:"DELETE",
                headers:{
                    "Content-Type":"application/json",
                },
                body:JSON.stringify({
                    "followingMemberId":followingMemberId
                })
            }).then((response) =>{
                console.log("response의 상태코드",response.status);
                if(response.ok){
                    Swal.fire({
                        title: `${followingMemberNickName}님을(를) 팔로우 취소하였습니다!`,
                        icon: 'success',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    }).then(function () {
                        if($('#follow').is(':checked')) {
                            window.location.href="/articles";
                        }
                    })
                    getFollows();
                }else{
                    Swal.fire({
                        title: `${followingMemberNickName}님을(를) 팔로우 취소하는데 실패하였습니다`,
                        icon: 'error',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                }
            })
        }
    });

}

//팝업창에서 맞팔하기
function popupFollowing(followerMemberId,followerMemberNickName){
    console.log("팔로잉 시작");
    Swal.fire({
        title: `${followerMemberNickName}님을(를) 팔로잉 하시겠습니까?`,
        icon: "question",
        showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
        cancelButtonColor: '#6e7881', // cancel 버튼 색깔 지정
        confirmButtonText: '팔로잉하기', // confirm 버튼 텍스트 지정
        cancelButtonText: '취소', // cancel 버튼 텍스트 지정
        reverseButtons: false, // 버튼 순서 거꾸로
        // background-color: #6e7881
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/api/members/${memberId}/following`,{
                method:"POST",
                headers:{
                    "Content-Type":"application/json",
                },
                body:JSON.stringify({
                    "followingMemberId":followerMemberId//팝업창에서는 팔로워지만 이제 팔로우가될것이다.
                })
            }).then((response) =>{
                console.log("response의 상태코드",response.status);
                if(response.status === 226){
                    Swal.fire({
                        title: `${followerMemberId}님은(는) 이미 팔로잉하고있는 유저입니다.`,
                        icon: 'info',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                }
                if(response.status === 400){
                    Swal.fire({
                        title: "잘못된 접근 방법입니다",
                        icon: 'error',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                }
                if(response.ok){
                    Swal.fire({
                        title: `${followerMemberNickName}님을(를) 팔로잉하는데 성공했습니다.`,
                        icon: 'success',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                    getFollows();
                    //게시글을 다시 가져오는 작업
                    if(hasArticle === true){
                        console.log("팔로잉 후 게시글 가져오기");
                        let obj = SearchCondSetting("click");
                        searchArticleAjax(obj);
                    }
                }
            })
        }
    });

}


//팔로워 버튼 클릭시(나를 팔로잉하고있는 유저 출력)
document.querySelector('.follow-container-follower').addEventListener("click",()=>{
    console.log("팔로워 버튼 클릭!");
    getFollows();
});

//팔로우 버튼 클릭시(내가 팔로잉하고있는 유저 출력)
document.querySelector('.follow-container-following').addEventListener("click",()=>{
    console.log("팔로우 버튼 클릭!");
    getFollows();
});

document.querySelector('#follow-search-btn').addEventListener("click",()=>{
    console.log("팔로우 팔로워 검색 버튼 클릭!");
    getFollows();

});

getFollows();