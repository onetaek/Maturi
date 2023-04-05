//팝업 클릭, 닫기 버튼
function followPopupToggle(){
    console.log("follow/following 클릭!");
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
        console.log("클릭한 follow버튼",followRadioValue );
    }
    if($('#following').is(':checked')){
        followRadioValue = document.querySelector('#following').value;
        console.log("클릭한 follow버튼",followRadioValue );
    }
    keywordFollowValue = document.querySelector('.follow-search-input').value;
    console.log("팔로우 검색 조건",keywordFollowValue);
}

<!--팔로우, 팔로워 목록을 가져오는 함수-->
function getFollows(){
    console.log("getFollows시작");
    settingFollowCond();
    fetch(`/api/members/${memberId}/follows?follow=${followRadioValue}&keyword=${keywordFollowValue}`)
        .then((response) => {
            if(response.status === 400){
                alert("잘못된 접근 방법입니다");
                return false;
            }
            return response.json()
        })
        .then((members) => {
            console.log("팔로우냐?팔로잉이냐?",followRadioValue);
            console.log("팔로워 맞냐?",followRadioValue === "follower")
            console.log("팔로잉 맞냐?",followRadioValue === "following")
            console.log(members);
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
                    html += `<img src="/test/file/${member.profileImg}" alt="프로필 이미지">`
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