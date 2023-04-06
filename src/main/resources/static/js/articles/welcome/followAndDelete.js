function deleteArticle(articleId){
    if(confirm("게시글을 삭제하시겠습니까?")){
        console.log("게시글 삭제");

        let articleDeleteForm = document.articleUpdateForm;
        console.log(articleDeleteForm);
        articleDeleteForm.action = `/articles/${articleId}`;
        articleDeleteForm.submit();
    }
}

//게시글의 유저 팔로잉
function following(obj,articleId,articleMemberId,articleMemberNickName){
    console.log("팔로잉 시작");
    console.log("this",obj);
    console.log("게시글의 id",articleId);
    console.log("게시글의 memberId",articleMemberId);
    if(confirm(`${articleMemberNickName}님을(를) 팔로잉 하시겠습니까?`)){
        fetch(`/api/members/${memberId}/following`,{
            method:"POST",
            headers:{
                "Content-Type":"application/json",
            },
            body:JSON.stringify({
                "followingMemberId":articleMemberId
            })
        }).then((response) =>{
            console.log("response의 상태코드",response.status);
            if(response.status === 226){
                alert(`${articleMemberNickName}님은(는) 이미 팔로잉하고있는 유저입니다.`);
            }
            if(response.status === 400){
                alert("잘못된 접근 방법입니다");
            }
            if(response.ok){
                alert(`${articleMemberNickName}님을(를) 팔로잉하는데 성공했습니다.`);
                addPage("click");
                getFollows();
            }
        })
    }
}

//게시글의 유저 팔로잉
function followCancel(articleId,articleMemberId,articleMemberNickName){
    console.log("팔로잉취소 시작");
    console.log("게시글의 memberId",articleMemberId);
    if(confirm(`${articleMemberNickName}님을(를) 팔로우 취소 하시겠습니까?`)){
        fetch(`/api/members/${memberId}/following`,{
            method:"DELETE",
            headers:{
                "Content-Type":"application/json",
            },
            body:JSON.stringify({
                "followingMemberId":articleMemberId
            })
        }).then((response) =>{
            console.log("response의 상태코드",response.status);
            if(response.ok){
                alert(`${articleMemberNickName}님을(를) 팔로우 취소하였습니다!`);
                // if($('#follow').is(':checked')) {
                //     window.location.href="/articles";
                // }
                // let element = obj.closest('.followingBtnWrap');
                // element.classList.replace(`followCancelBtnWrap${articleId}`, `followingBtnWrap${articleId}`);
                // element.innerHTML = `<li class="followingBtnWrap${articleId}"><div onclick="following(this,${articleId},${articleMemberId},'${articleMemberNickName}')"><ion-icon name="person-add-outline"></ion-icon><span>팔로잉</span></div></li>`;
                getFollows();
                if(hasArticle === true){
                    console.log("팔로잉취소 후 게시글 가져오기");
                    let obj = searchCondSetting("click");
                    searchArticleAjax(obj);
                }
            }else{
                alert(`${articleMemberNickName}님을(를) 팔로우 취소하는데 실패하였습니다`);
            }
        })
    }
}

//팝업창에서 팔로우 취소
function popupFollowCancel(followingMemberId,followingMemberNickName){
    console.log("팝업 팔로우 취소 시작");
    if(confirm(`${followingMemberNickName}님을(를) 팔로우 취소 하시겠습니까?`)){
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
                alert(`${followingMemberNickName}님을(를) 팔로우 취소하였습니다!`);
                if($('#follow').is(':checked')) {
                    window.location.href="/articles";
                }
                getFollows();
            }else{
                alert(`${followingMemberNickName}님을(를) 팔로우 취소하는데 실패하였습니다`);
            }
        })
    }
}

//팝업창에서 맞팔하기
function popupFollowing(followerMemberId,followerMemberNickName){
    console.log("팔로잉 시작");
    if(confirm(`${followerMemberNickName}님을(를) 팔로잉 하시겠습니까?`)){
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
                alert(`${followerMemberId}님은(는) 이미 팔로잉하고있는 유저입니다.`);
            }
            if(response.status === 400){
                alert("잘못된 접근 방법입니다");
            }
            if(response.ok){
                alert(`${followerMemberNickName}님을(를) 팔로잉하는데 성공했습니다.`);
                getFollows();
                //게시글을 다시 가져오는 작업
                if(hasArticle === true){
                    console.log("팔로잉 후 게시글 가져오기");
                    let obj = searchCondSetting("click");
                    searchArticleAjax(obj);
                }
            }
        })
    }
}
